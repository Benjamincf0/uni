library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity full_adder is
    port (
        a     : in  std_logic;
        b     : in  std_logic;
        c_in  : in  std_logic;
        s     : out std_logic;  -- Sum
        c_out : out std_logic   -- Carry-out
    );
end full_adder;

architecture Structural of full_adder is
    component half_adder        -- Component declaration for half-adder
        port (
            a : in  std_logic;
            b : in  std_logic;
            s : out std_logic;
            c : out std_logic
        );
		  
    end component;
    signal s1, c1, c2 : std_logic;     -- Internal signals
begin
 
    HA1: half_adder port map (   -- First half-adder (a + b)
        a => a,
        b => b,
        s => s1,
        c => c1
    );

    HA2: half_adder port map (  -- Second half-adder (s1 + c_in)
        a => s1,
        b => c_in,
        s => s,
        c => c2
    );
	 
    c_out <= c1 or c2;          -- Carry-out logic
end Structural;
