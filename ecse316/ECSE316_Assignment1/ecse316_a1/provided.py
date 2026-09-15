"""Protocol definitions supplied for ECSE 316 Assignment 1.

You may read this file for context. Do not modify it.
"""

import struct
import zlib
from enum import IntEnum


HEADER_STRUCT = struct.Struct("!BII")
ACK_STRUCT = struct.Struct("!II")
MAX_PAYLOAD_SIZE = 1024 * 1024


class MessageType(IntEnum):
    DATA = 1
    ACK = 2
    CLOSE = 3


class ProtocolError(Exception):
    """The peer sent bytes that violate the assignment protocol."""


class UnexpectedEOF(ProtocolError):
    """The stream ended before a required byte sequence was complete."""


class ChecksumError(ProtocolError):
    """A complete payload did not match the CRC in its header."""


def payload_crc32(payload: bytes) -> int:
    return zlib.crc32(payload) & 0xFFFFFFFF


def validate_payload_length(message_type: MessageType, length: int) -> None:
    if length > MAX_PAYLOAD_SIZE:
        raise ProtocolError("payload exceeds the 1 MiB maximum")
    if message_type is MessageType.ACK and length != ACK_STRUCT.size:
        raise ProtocolError("ACK payload must contain exactly eight bytes")
    if message_type is MessageType.CLOSE and length != 0:
        raise ProtocolError("CLOSE payload must be empty")
