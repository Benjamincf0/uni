import pandas as pd
import numpy as np

file_path = '/Users/benjamin/uni/FALL2025/ECSE310/data set homework 3 Fall 2025.csv'

def smi(data: np.ndarray) -> float:
    """compute the SMI of the given array of outcomes"""
    N = len(data)
    # for each unique value in each column, i count the frequency of each and divide it by the total
    # probs = np.array([np.sum(data == value) / N for value in np.unique(data)])
    probs = np.vectorize(lambda value: np.sum(data == value) / N)(np.unique(data))
    # then i compute xlog2x elementwise and sum it
    smi = -1*np.sum(probs*np.log2(probs))
    return smi

def all_smi(df: pd.DataFrame):
    smis  = df.apply(smi, axis=0)
    return smis

def joint_smi(df: pd.DataFrame):
    # get the prob of each row combination
    prob = np.asarray(df.value_counts(normalize=True).values) # no copying with np.asarray
    # compute smi with the same formula
    return np.sum(-1*prob*np.log2(prob))

def mutual_information(df: pd.DataFrame):
    return (smi(df[df.columns[0]].to_numpy()) +
        smi(df[df.columns[1]].to_numpy()) - 
        joint_smi(df) )

def main():
    # Load the data
    df = pd.read_csv(file_path, dtype=str)
    print('A) All SMIs:')
    print(all_smi(df))

    print(f"\nB) Join entropy of entire data set: {joint_smi(df):.5f}")

    print("\nC) Mutual information:")
    [print(f"   I({name} ; Income ) {mutual_information(df[[name, 'income']]):.5f}")
      for name in df.columns[1:-1]]

    HH = all_smi(df)['hours-per-week']
    HH_given_F = smi(df[df['gender'] == 'Female']['hours-per-week'].to_numpy())
    # should be negative because learning reduces the uncertainty => less entropy
    print(f"\nD) Gain = H(#h) - H( #h | Gender = 'female') {HH:.3f} - {HH_given_F:.3f} = {HH - HH_given_F:.4f} bits")


if __name__ == "__main__":
    main()