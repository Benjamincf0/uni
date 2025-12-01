library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity seven_segment_decoder is
    port (
        code : in STD_LOGIC_VECTOR (3 downto 0);
        segments_out : out STD_LOGIC_VECTOR (6 downto 0)
    );
end seven_segment_decoder;

architecture decoder of seven_segment_decoder is
begin
    WITH code SELECT
        segments_out <=
            "1000000" WHEN "0000", -- 0
            "1111001" WHEN "0001", -- 1
            "0100100" WHEN "0010", -- 2
            "0110000" WHEN "0011", -- 3
            "0011001" WHEN "0100", -- 4
            "0010010" WHEN "0101", -- 5
            "0000010" WHEN "0110", -- 6
            "1111000" WHEN "0111", -- 7
            "0000000" WHEN "1000", -- 8
            "0010000" WHEN "1001", -- 9
            "1111111" WHEN others; -- Default case
end decoder;