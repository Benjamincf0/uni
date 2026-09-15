"""Supplied checks, networking setup, experiment, plots, and submission helper.

Students do not need to read or modify this file.
"""

from __future__ import annotations

import importlib
import platform
import random
import socket
import statistics
import sys
import threading
import time
from collections import deque
from contextlib import contextmanager
from types import SimpleNamespace

from .provided import (
    ACK_STRUCT,
    HEADER_STRUCT,
    MAX_PAYLOAD_SIZE,
    ChecksumError,
    MessageType,
    ProtocolError,
    UnexpectedEOF,
    payload_crc32,
)


def _load_student_code():
    """Reload the three files students edit and return their current functions."""

    importlib.invalidate_caches()
    from . import protocol, server, stream_io

    stream_io = importlib.reload(stream_io)
    protocol = importlib.reload(protocol)
    server = importlib.reload(server)
    return SimpleNamespace(
        send_all=stream_io.send_all,
        receive_exactly=stream_io.receive_exactly,
        encode_frame=protocol.encode_frame,
        send_frame=protocol.send_frame,
        receive_frame=protocol.receive_frame,
        handle_client=server.handle_client,
    )


def show_environment() -> None:
    print(f"Python {platform.python_version()} on {platform.platform()}")
    print(f"Protocol header: {HEADER_STRUCT.size} bytes")
    assert HEADER_STRUCT.size == 9
    assert payload_crc32(b"") == 0
    print("Assignment folder imported successfully.")


def show_stream_demo() -> None:
    listener = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    listener.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    listener.bind(("127.0.0.1", 0))
    listener.listen()
    sender = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    receiver = None
    try:
        sender.connect(listener.getsockname())
        receiver, _ = listener.accept()
        receiver.settimeout(2.0)
        sender.sendall(b"ABC")
        sender.sendall(b"DEFG")

        received_chunks = []
        received_count = 0
        requested_sizes = deque([2, 5])
        while received_count < 7:
            requested = requested_sizes.popleft() if requested_sizes else 5
            chunk = receiver.recv(min(requested, 7 - received_count))
            if not chunk:
                raise ConnectionError("stream ended before all seven bytes arrived")
            received_chunks.append(chunk)
            received_count += len(chunk)
    finally:
        sender.close()
        if receiver is not None:
            receiver.close()
        listener.close()
    print("Two writes: ", [b"ABC", b"DEFG"])
    print("recv results:", received_chunks)
    assert b"".join(received_chunks) == b"ABCDEFG"


def show_frame_example() -> None:
    payload = b"ABC"
    checksum = payload_crc32(payload)
    header = HEADER_STRUCT.pack(
        int(MessageType.DATA),
        len(payload),
        checksum,
    )
    print("Payload:       ", payload)
    print("Header fields: ", f"type=DATA, length={len(payload)}, crc32={checksum:08x}")
    print("Header bytes:  ", header.hex(" "))
    print("Complete frame:", (header + payload).hex(" "))


class ShortWritingSocket:
    def __init__(self, write_sizes):
        self._write_sizes = deque(write_sizes)
        self.sent = bytearray()
        self.offered_sizes = []

    def send(self, data):
        offered = bytes(data)
        self.offered_sizes.append(len(offered))
        limit = self._write_sizes.popleft() if self._write_sizes else len(offered)
        accepted = min(limit, len(offered))
        self.sent.extend(offered[:accepted])
        return accepted


class FragmentingReceiveSocket:
    def __init__(self, source: bytes, fragment_sizes):
        self._remaining = bytearray(source)
        self._fragment_sizes = deque(fragment_sizes)
        self.request_sizes = []

    def recv(self, count: int) -> bytes:
        self.request_sizes.append(count)
        if not self._remaining:
            return b""
        limit = self._fragment_sizes.popleft() if self._fragment_sizes else count
        if limit == 0:
            return b""
        amount = min(count, limit, len(self._remaining))
        result = bytes(self._remaining[:amount])
        del self._remaining[:amount]
        return result


def check_send_all() -> None:
    api = _load_student_code()
    writer = ShortWritingSocket([2, 1, 3, 2])
    api.send_all(writer, b"abcdefgh", chunk_size=4)
    assert bytes(writer.sent) == b"abcdefgh"
    assert max(writer.offered_sizes) <= 4

    failed = ShortWritingSocket([2, 0])
    try:
        api.send_all(failed, b"abcd", chunk_size=4)
        raise AssertionError("A zero write should raise ConnectionError")
    except ConnectionError:
        pass
    print("send_all checks passed.")


def check_receive_exactly() -> None:
    api = _load_student_code()
    reader = FragmentingReceiveSocket(b"abcdefgh", [1, 2, 1, 4])
    assert api.receive_exactly(reader, 8, chunk_size=3) == b"abcdefgh"
    assert max(reader.request_sizes) <= 3

    empty = FragmentingReceiveSocket(b"unused", [])
    assert api.receive_exactly(empty, 0, chunk_size=4) == b""
    assert empty.request_sizes == []

    premature = FragmentingReceiveSocket(b"abcdef", [2, 0])
    try:
        api.receive_exactly(premature, 6, chunk_size=4)
        raise AssertionError("Premature EOF should raise UnexpectedEOF")
    except UnexpectedEOF:
        pass
    print("receive_exactly checks passed.")


def check_encode_frame() -> None:
    api = _load_student_code()
    payload = b"ABC"
    expected_header = HEADER_STRUCT.pack(
        int(MessageType.DATA), len(payload), payload_crc32(payload)
    )
    assert api.encode_frame(MessageType.DATA, payload) == expected_header + payload
    assert api.encode_frame(MessageType.CLOSE, b"") == HEADER_STRUCT.pack(
        int(MessageType.CLOSE), 0, 0
    )
    print("encode_frame checks passed.")


def check_send_frame() -> None:
    api = _load_student_code()
    payload = b"ABC"
    expected = HEADER_STRUCT.pack(
        int(MessageType.DATA), len(payload), payload_crc32(payload)
    ) + payload
    writer = ShortWritingSocket([2, 1, 3, 2])
    api.send_frame(
        writer,
        MessageType.DATA,
        payload,
        chunk_size=4,
    )
    assert bytes(writer.sent) == expected
    assert max(writer.offered_sizes) <= 4
    print("send_frame checks passed.")


def check_receive_frame() -> None:
    api = _load_student_code()
    payload = bytes(range(256))
    wire = HEADER_STRUCT.pack(
        int(MessageType.DATA), len(payload), payload_crc32(payload)
    ) + payload
    reader = FragmentingReceiveSocket(
        wire, [1, 2, 1, 3, 1, 5, 8, 13, 21, 34, 55, 89]
    )
    assert api.receive_frame(reader, chunk_size=17) == (MessageType.DATA, payload)

    damaged = HEADER_STRUCT.pack(
        int(MessageType.DATA), 3, payload_crc32(b"ABC")
    ) + b"ABD"
    try:
        api.receive_frame(FragmentingReceiveSocket(damaged, [20]), 20)
        raise AssertionError("A damaged payload should raise ChecksumError")
    except ChecksumError:
        pass
    print("receive_frame checks passed.")


@contextmanager
def _running_server(handler, server_chunk_size: int = 4096):
    listener = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    listener.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    listener.bind(("127.0.0.1", 0))
    listener.listen()
    listener.settimeout(0.1)
    address = listener.getsockname()
    stop_event = threading.Event()
    failures = []

    def serve():
        while not stop_event.is_set():
            try:
                connected, _ = listener.accept()
            except socket.timeout:
                continue
            except OSError:
                break
            with connected:
                connected.settimeout(5.0)
                try:
                    handler(connected, server_chunk_size)
                except BaseException as error:
                    failures.append(error)
                    break

    worker = threading.Thread(target=serve, daemon=True)
    worker.start()
    try:
        yield address
    finally:
        stop_event.set()
        listener.close()
        worker.join(timeout=2.0)
        if worker.is_alive():
            raise RuntimeError("background server did not stop")
        if failures:
            raise failures[0]


def _validate_ack(
    payload: bytes,
    response: tuple[MessageType, bytes],
) -> tuple[int, int]:
    response_type, response_payload = response
    if response_type is not MessageType.ACK:
        raise ProtocolError(f"expected ACK, received {response_type.name}")
    acknowledged = ACK_STRUCT.unpack(response_payload)
    expected = (len(payload), payload_crc32(payload))
    if acknowledged != expected:
        raise ProtocolError(f"ACK {acknowledged} does not match {expected}")
    return acknowledged


def _send_records(api, address, payloads, client_chunk_size: int = 4096):
    acknowledgements = []
    with socket.create_connection(address, timeout=5.0) as client:
        client.settimeout(5.0)
        for payload in payloads:
            api.send_frame(
                client,
                MessageType.DATA,
                payload,
                client_chunk_size,
            )
            response = api.receive_frame(client, client_chunk_size)
            acknowledgements.append(_validate_ack(payload, response))

        api.send_frame(
            client,
            MessageType.CLOSE,
            b"",
            client_chunk_size,
        )
        response_type, response_payload = api.receive_frame(
            client, client_chunk_size
        )
        if response_type is not MessageType.CLOSE or response_payload:
            raise ProtocolError("server did not complete the CLOSE exchange")
    return acknowledgements


def run_real_tcp_demo():
    api = _load_student_code()
    payloads = [b"Hello from the ECSE 316 client.", bytes(range(256))]
    with _running_server(api.handle_client, server_chunk_size=7) as address:
        acknowledgements = _send_records(
            api, address, payloads, client_chunk_size=5
        )
    print("Real TCP server address:", address)
    print("Acknowledgements:", acknowledgements)
    assert acknowledgements == [
        (len(payload), payload_crc32(payload)) for payload in payloads
    ]
    print("Real TCP client/server demonstration passed.")
    return acknowledgements


def _run_record_workload(
    api,
    client,
    workload: bytes,
    record_size: int,
    chunk_size: int,
    link_bits_per_second: float,
    round_trip_seconds: float,
) -> tuple[int, int]:
    """Send one workload and return first-confirmation and completion times."""

    start_ns = time.perf_counter_ns()
    first_confirmation_ns = None
    ack_frame_bytes = HEADER_STRUCT.size + ACK_STRUCT.size
    for offset in range(0, len(workload), record_size):
        payload = workload[offset : offset + record_size]
        data_frame_bytes = HEADER_STRUCT.size + len(payload)
        time.sleep(
            round_trip_seconds / 2
            + 8 * data_frame_bytes / link_bits_per_second
        )
        api.send_frame(client, MessageType.DATA, payload, chunk_size)
        response = api.receive_frame(client, chunk_size)
        time.sleep(
            round_trip_seconds / 2
            + 8 * ack_frame_bytes / link_bits_per_second
        )
        _validate_ack(payload, response)
        if first_confirmation_ns is None:
            first_confirmation_ns = time.perf_counter_ns() - start_ns
    completion_ns = time.perf_counter_ns() - start_ns
    if first_confirmation_ns is None:
        raise ValueError("workload must contain at least one record")
    return first_confirmation_ns, completion_ns


def run_record_size_experiment(
    workload_bytes=65536,
    record_sizes=(64, 4096, 65536),
    chunk_size=65545,
    link_mbps=10.0,
    round_trip_ms=1.0,
    repetitions=5,
    seed=31601,
):
    """Measure one fixed workload divided into differently sized records."""

    if workload_bytes <= 0:
        raise ValueError("workload_bytes must be positive")
    if chunk_size <= 0:
        raise ValueError("chunk_size must be positive")
    if link_mbps <= 0 or round_trip_ms < 0:
        raise ValueError("link_mbps must be positive and round_trip_ms non-negative")
    if repetitions <= 0:
        raise ValueError("repetitions must be positive")
    if not record_sizes:
        raise ValueError("record_sizes must not be empty")
    for record_size in record_sizes:
        if record_size <= 0 or record_size > MAX_PAYLOAD_SIZE:
            raise ValueError("record sizes must be between 1 byte and 1 MiB")
        if workload_bytes % record_size:
            raise ValueError("each record size must divide the workload size")

    api = _load_student_code()
    workload = random.Random(seed).randbytes(workload_bytes)
    link_bits_per_second = link_mbps * 1_000_000
    round_trip_seconds = round_trip_ms / 1_000
    records = []
    with _running_server(
        api.handle_client,
        server_chunk_size=chunk_size,
    ) as address:
        for record_size in record_sizes:
            records_per_workload = workload_bytes // record_size
            with socket.create_connection(address, timeout=5.0) as client:
                client.settimeout(5.0)
                for trial in range(1, repetitions + 1):
                    first_confirmation_ns, completion_ns = _run_record_workload(
                        api,
                        client,
                        workload,
                        record_size,
                        chunk_size,
                        link_bits_per_second,
                        round_trip_seconds,
                    )
                    records.append(
                        {
                            "workload_bytes": workload_bytes,
                            "record_payload_bytes": record_size,
                            "records_per_workload": records_per_workload,
                            "trial": trial,
                            "first_confirmation_ns": first_confirmation_ns,
                            "completion_ns": completion_ns,
                        }
                    )
                api.send_frame(
                    client,
                    MessageType.CLOSE,
                    b"",
                    chunk_size,
                )
                response_type, _ = api.receive_frame(client, chunk_size)
                if response_type is not MessageType.CLOSE:
                    raise ProtocolError("invalid CLOSE response")
    return records


def summarize_records(records):
    summary = []
    record_sizes = sorted(
        {record["record_payload_bytes"] for record in records}
    )
    fixed_bytes_per_exchange = 2 * HEADER_STRUCT.size + ACK_STRUCT.size
    for record_size in record_sizes:
        group = [
            record
            for record in records
            if record["record_payload_bytes"] == record_size
        ]
        workload_bytes = group[0]["workload_bytes"]
        records_per_workload = group[0]["records_per_workload"]
        protocol_bytes = (
            workload_bytes
            + records_per_workload * fixed_bytes_per_exchange
        )
        total_measured_seconds = (
            sum(record["completion_ns"] for record in group)
            / 1_000_000_000
        )
        summary.append(
            {
                "record_payload_bytes": record_size,
                "records_per_workload": records_per_workload,
                "protocol_efficiency_percent": (
                    100 * workload_bytes / protocol_bytes
                ),
                "median_first_confirmation_ms": statistics.median(
                    record["first_confirmation_ns"] / 1_000_000
                    for record in group
                ),
                "median_completion_ms": statistics.median(
                    record["completion_ns"] / 1_000_000
                    for record in group
                ),
                "workload_goodput_mbps": (
                    8 * workload_bytes * len(group)
                    / total_measured_seconds
                    / 1_000_000
                ),
            }
        )
    return summary


def display_summary_and_plots(records):
    import matplotlib.pyplot as plt

    summary = summarize_records(records)
    print(
        "record (B) | records | efficiency (%) | first ACK (ms) "
        "| completion (ms) | goodput (Mbit/s)"
    )
    print("-" * 100)
    for row in summary:
        print(
            f"{row['record_payload_bytes']:10d} | "
            f"{row['records_per_workload']:7d} | "
            f"{row['protocol_efficiency_percent']:14.3f} | "
            f"{row['median_first_confirmation_ms']:14.4f} | "
            f"{row['median_completion_ms']:15.4f} | "
            f"{row['workload_goodput_mbps']:18.3f}"
        )

    record_sizes = [row["record_payload_bytes"] for row in summary]
    figure, axes = plt.subplots(1, 2, figsize=(11, 4), constrained_layout=True)
    axes[0].plot(
        record_sizes,
        [row["median_first_confirmation_ms"] for row in summary],
        marker="o",
    )
    axes[1].plot(
        record_sizes,
        [row["workload_goodput_mbps"] for row in summary],
        marker="o",
    )
    axes[0].set_title("First-confirmation latency")
    axes[0].set_ylabel("Median time to first ACK (ms)")
    axes[1].set_title("Workload goodput")
    axes[1].set_ylabel("Useful payload rate (Mbit/s)")
    for axis in axes:
        axis.set_xscale("log", base=2)
        axis.set_xlabel("Record payload size (bytes)")
        axis.grid(True, alpha=0.25)
    plt.show()
    return summary, figure


def run_final_checks(team_number, team_members, experiment_records) -> None:
    assert team_number not in {"", "XX"}, "Replace the team-number placeholder."
    assert 1 <= len(team_members) <= 3, "List one to three team members."
    assert all(
        isinstance(member, (list, tuple)) and len(member) == 2
        for member in team_members
    ), "Write each team member as (name, student ID)."
    assert all(
        name not in {"", "Name"} and student_id not in {"", "Student ID"}
        for name, student_id in team_members
    ), "Replace every team-member placeholder."
    assert len(experiment_records) == 15, "Run the complete experiment cell."
    api = _load_student_code()
    with _running_server(api.handle_client, server_chunk_size=11) as address:
        result = _send_records(
            api, address, [b"final check"], client_chunk_size=7
        )
    assert result == [(11, payload_crc32(b"final check"))]
    print("All executable submission checks passed.")
    print("Now verify the written answers and visible plots.")
