library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity benjamin_curis_friedman_wrapper is
    port (
        A, B : in STD_LOGIC_VECTOR (3 downto 0);
        decoded_A : out STD_LOGIC_VECTOR (6 downto 0);
        decoded_B : out STD_LOGIC_VECTOR (6 downto 0);
        decoded_AplusB : out STD_LOGIC_VECTOR (13 downto 0)
    );
end benjamin_curis_friedman_wrapper;

architecture Structural of benjamin_curis_friedman_wrapper is

	component seven_segment_decoder
		port (
        code : in STD_LOGIC_VECTOR (3 downto 0);
        segments_out : out STD_LOGIC_VECTOR (6 downto 0)
		);
	end component;
	
	component benjamin_curis_friedman_bcd_adder
		port (
        A : in  std_logic_vector(3 downto 0);
        B : in  std_logic_vector(3 downto 0);
        S : out std_logic_vector(3 downto 0);
        C : out std_logic
		);
	end component;
	
	signal AplusB_sum: std_logic_vector(3 downto 0);
	signal AplusB_carry: std_logic;
	signal AplusB_carry_to_vector: std_logic_vector(3 downto 0);
	
	signal decodedAplusB_1: std_logic_vector(6 downto 0);
	signal decodedAplusB_2: std_logic_vector(6 downto 0);

	
begin

	ssd1: seven_segment_decoder port map (code => A, segments_out => decoded_A);
	ssd2: seven_segment_decoder port map (code => B, segments_out => decoded_B);
	
	bcda: benjamin_curis_friedman_bcd_adder port map (A => A, B => B, S => AplusB_sum, C => AplusB_carry);
	
	AplusB_carry_to_vector <= "000" & AplusB_carry;
	
	ssd3: seven_segment_decoder port map (code => AplusB_sum, segments_out => decodedAplusB_1);
	ssd4: seven_segment_decoder port map (code => AplusB_carry_to_vector, segments_out => decodedAplusB_2);
	
	decoded_AplusB <= decodedAplusB_1 & decodedAplusB_2;

end Structural;