library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity benjamin_curis_friedman_bcd_adder is
    port (
        A : in  std_logic_vector(3 downto 0);
        B : in  std_logic_vector(3 downto 0);
        S : out std_logic_vector(3 downto 0);
        C : out std_logic
    );
end benjamin_curis_friedman_bcd_adder;

architecture Structural of benjamin_curis_friedman_bcd_adder is

    component full_adder
        port (
            a     : in  std_logic;
            b     : in  std_logic;
            c_in  : in  std_logic;
            s     : out std_logic;
            c_out : out std_logic
        );
    end component;

    signal bin_sum   : std_logic_vector(3 downto 0);
    signal c0, c1, c2, carry0 : std_logic;

    signal corr : std_logic;

    signal correction_factor : std_logic_vector(3 downto 0);

    signal corrected_sum : std_logic_vector(3 downto 0);
    signal dummy_c0, dummy_c1, dummy_c2, c_corr : std_logic;

begin

    fa0: full_adder port map (a => A(0), b => B(0), c_in => '0', s => bin_sum(0), c_out => c0);
    fa1: full_adder port map (a => A(1), b => B(1), c_in => c0, s => bin_sum(1), c_out => c1);
    fa2: full_adder port map (a => A(2), b => B(2), c_in => c1, s => bin_sum(2), c_out => c2);
    fa3: full_adder port map (a => A(3), b => B(3), c_in => c2, s => bin_sum(3), c_out => carry0);

    corr <= carry0 or (bin_sum(3) and (bin_sum(2) or bin_sum(1)));

    correction_factor <= "0110" when corr = '1' else "0000";
	 
    fa4: full_adder port map (a => bin_sum(0), b => correction_factor(0), c_in => '0', s => corrected_sum(0), c_out => dummy_c0);
    fa5: full_adder port map (a => bin_sum(1), b => correction_factor(1), c_in => dummy_c0, s => corrected_sum(1), c_out => dummy_c1);
    fa6: full_adder port map (a => bin_sum(2), b => correction_factor(2), c_in => dummy_c1, s => corrected_sum(2), c_out => dummy_c2);
    fa7: full_adder port map (a => bin_sum(3), b => correction_factor(3), c_in => dummy_c2, s => corrected_sum(3), c_out => c_corr);

    S <= corrected_sum;
    C <= corr OR c_corr;

end Structural;
