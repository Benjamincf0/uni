library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity benjamin_curis_friedman_comparator is
  Port ( 
    A           : in  std_logic_vector(3 downto 0);
    B           : in  std_logic_vector(3 downto 0);
    AgtBplusOne : out std_logic;  -- A > (B+1)
    AgteBplusOne: out std_logic;  -- A >= (B+1)
    AltBplusOne : out std_logic;  -- A < (B+1)
    AlteBplusOne: out std_logic;  -- A <= (B+1)
    AeqBplusOne : out std_logic;  -- A = (B+1)
    overflow    : out std_logic   -- overflow on (B+1)
  );
end benjamin_curis_friedman_comparator;

architecture Behavioral of benjamin_curis_friedman_comparator is
begin
  process(A, B)
    variable B_val   : unsigned(3 downto 0);
    variable A_val   : unsigned(3 downto 0);
    variable result  : unsigned(4 downto 0);
  begin

  B_val := unsigned(B);
    A_val := unsigned(A);
    
    result := ('0' & B_val) + 1;
    
    if result(4) = '1' then
      overflow    <= '1';
      AgtBplusOne <= '0';
      AgteBplusOne<= '0';
      AltBplusOne <= '0';
      AlteBplusOne<= '0';
      AeqBplusOne <= '0';
    else
      overflow    <= '0';
		
      if A_val > result(3 downto 0) then
        AgtBplusOne <= '1';
      else
        AgtBplusOne <= '0';
      end if;
      
      if A_val >= result(3 downto 0) then
        AgteBplusOne <= '1';
      else
        AgteBplusOne <= '0';
      end if;
      
      if A_val < result(3 downto 0) then
        AltBplusOne <= '1';
      else
        AltBplusOne <= '0';
      end if;
      
      if A_val <= result(3 downto 0) then
        AlteBplusOne <= '1';
      else
        AlteBplusOne <= '0';
      end if;
      
      if A_val = result(3 downto 0) then
        AeqBplusOne <= '1';
      else
        AeqBplusOne <= '0';
      end if;
    end if;
  end process;
end Behavioral;
