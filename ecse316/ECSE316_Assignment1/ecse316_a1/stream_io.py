"""Student task 1: reliable operations on a stream socket."""

from .provided import UnexpectedEOF


def send_all(sock, data: bytes, chunk_size: int) -> None:
    """Send every byte while offering at most chunk_size bytes per call."""

    if chunk_size <= 0:
        raise ValueError("chunk_size must be positive")

    # TODO: repeatedly call sock.send until every byte has been accepted.
    # A zero return before completion means the connection has failed.
    raise NotImplementedError("Complete send_all")


def receive_exactly(sock, count: int, chunk_size: int) -> bytes:
    """Receive exactly count bytes or raise UnexpectedEOF.

    Each recv call must request at most chunk_size bytes.
    """

    if chunk_size <= 0:
        raise ValueError("chunk_size must be positive")
    if count < 0:
        raise ValueError("count must not be negative")

    # TODO: collect bytes until count has been reached. An empty result
    # from recv before completion means premature EOF.
    raise NotImplementedError("Complete receive_exactly")
