#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <ctype.h>

int is_anagram(char *s1, char *s2)
{
	int f1[26] = {0};
	int f2[26] = {0};

	while (*s1 != '\0') {
		f1[*s1-'a']++;
		s1++;
	}

	while (*s2 != '\0') {
		f2[*s2-'a']++;
		s2++;
	}

	for (int i = 0; i < 26; i++) {
		if (f1[i] != f2[i]) return 0;
	}

	return 1;
}

int is_all_lowercase(char *s)
{
	while (*s != '\0') {
		if (islower(*s) == 0) {
			return 0;
		} // if not -> false
		s++;
	}
	return 1;
}

long gcd(long n1, long n2)
{	
	if (n1 == 0 && n2 != 0) return n2;
	if (n1 != 0 && n2 == 0) return n1;
	if (n1 == 0 && n2 == 0) return 0;

	if (n2 > n1) {
		long temp = n1;
		n1 = n2;
		n2 = temp;
	}

	while (n2 != 0 && n1 != 0) {
		ldiv_t result;
		result = ldiv(n1, n2);
		// long q = result.quot;
		long r = result.rem;
		n1 = n2;
		n2 = r;
	}

	if (n1 == 0) {
		return n2;
	}

	return n1;
}

long gcd_many(int n, long ns[])
{
	long gcd_val = ns[0];
	for (int i = 1; i < n; i++) {
		gcd_val = gcd(gcd_val, ns[i]);
	}

	return gcd_val;
}

int handle_sqrt(int argc, char *argv[])
{
	if (argc != 2) {
		fprintf(stderr, "error: wrong count of inputs to sqrt\n");
		return 2;
	};
	char *start = argv[1];
	char *end;
	double val = strtod(start, &end);

	if (end == start || *end != '\0' || val < 0) {
		fprintf(stderr, "error: invalid input to sqrt\n");
		return 3;
	}

	double result = sqrt(val);
	printf("%.2f\n", result);
	return 0;
}

int handle_anagram(int argc, char *argv[])
{
	if (argc != 3) {
		fprintf(stderr, "error: wrong count of inputs to anagram\n");
		return 2;
	};

	char *s1 = argv[1];
	char *s2 = argv[2];

	if (is_all_lowercase(s1) == 0 || is_all_lowercase(s2) == 0) { // if either of them is false
		fprintf(stderr, "error: invalid input to anagram\n");
		return 3;
	}


	if (is_anagram(s1, s2)) {
		puts("true");
		return 0;
	};
	puts("false");
	return 0;

}

int handle_gcd(int argc, char *argv[])
{
	if (argc < 2) {
		fprintf(stderr, "error: wrong count of inputs to gcd\n");
		return 2;
	};

	int len = argc - 1;
	long nums[len];
	for (int i = 0; i < len; i++) {
		char *start = argv[1+i];
		char *end;
		nums[i] = strtol(start, &end, 10);

		if (end == start || *end != '\0' || nums[i] <= 0) {
			fprintf(stderr, "error: invalid input to gcd\n");
			return 3;
		}
	}

	long gcd_val = gcd_many(len, nums);
	printf("%lu\n", gcd_val);

	return 0;
}

int main(int argc, char *argv[])
{
	while (1<20) {
		fprintf(stdout, "> ");
		char line[100];
		char *words[20];
		int count = 0;
		fgets(line, sizeof(line), stdin);
		
		// split on space/newline
		char *token = strtok(line, " \n");
		while (token != NULL && count < 20) {
			words[count++] = token;      // store pointer to the word
			token = strtok(NULL, " \n");
		}


		// int ret;
		if (count < 1) {
		} else if (strcmp(words[0], "sqrt") == 0) {
			handle_sqrt(count, words);
		} else if (strcmp(words[0], "gcd") == 0) {
			handle_gcd(count, words);
		} else if (strcmp(words[0], "anagram") == 0) {
			handle_anagram(count, words);
		} else if (strcmp(words[0], "exit") == 0) {
			exit(0);
		} else {
			fprintf(stderr, "error: unknown command %s\n", words[0]);
			
		}
	}
}
