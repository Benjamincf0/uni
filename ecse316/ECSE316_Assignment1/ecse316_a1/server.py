"""Student task 3: serve one connected protocol client."""

from .provided import ACK_STRUCT, MessageType, ProtocolError, payload_crc32
from .protocol import receive_frame, send_frame


def handle_client(sock, chunk_size: int = 4096) -> None:
    """Serve DATA records until the client completes a CLOSE exchange."""

    # TODO: receive (message_type, payload) tuples in a loop. Reply to DATA
    # with an ACK containing the payload length and CRC. Reply to CLOSE with
    # CLOSE, then return. A client is not allowed to send ACK.
    raise NotImplementedError("Complete handle_client")
