"""Student task 2: encode and recover framed application messages."""

from .provided import (
    HEADER_STRUCT,
    ChecksumError,
    MessageType,
    ProtocolError,
    payload_crc32,
    validate_payload_length,
)
from .stream_io import receive_exactly, send_all


def encode_frame(message_type: MessageType, payload: bytes) -> bytes:
    """Return one complete protocol frame in network byte order."""

    if not isinstance(payload, bytes):
        raise TypeError("payload must be bytes")

    # TODO: validate the message type and payload length, calculate the
    # payload CRC-32, pack the header, and append the payload.
    raise NotImplementedError("Complete encode_frame")


def send_frame(
    sock,
    message_type: MessageType,
    payload: bytes,
    chunk_size: int,
) -> None:
    """Encode and send one complete frame."""

    # TODO: encode the frame, then send every encoded byte.
    raise NotImplementedError("Complete send_frame")


def receive_frame(sock, chunk_size: int) -> tuple[MessageType, bytes]:
    """Return the message type and payload from one validated frame."""

    # TODO: receive the fixed-length header, unpack and validate its fields,
    # receive the declared payload, and verify its CRC-32.
    raise NotImplementedError("Complete receive_frame")
