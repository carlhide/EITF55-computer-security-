package application;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PrimeGenerator {

	private static final BigInteger ZERO = BigInteger.ZERO;
	private static final BigInteger ONE = BigInteger.ONE;
	private static final BigInteger TWO = new BigInteger("2");
	private static final BigInteger THREE = new BigInteger("3");


	public static List<BigInteger> getPrimes(int amount, int bitSize, int iterations){


		int count = 0;
		int count2 = 0;
		Random rand = new Random(Long.MAX_VALUE);
		List<BigInteger> primes = new ArrayList<BigInteger>();
		BigInteger test;
		while(count<amount && count2<(Math.pow(2, bitSize))) {
			test = new BigInteger(bitSize, rand);
			if(isRabinMiller(test, iterations) && !primes.contains(test)){
				primes.add(test);
				count++;
				System.out.println((count*100/amount) + " % \t Prime found: " + test.toString());
			}
			count2++;
		}
		return primes;
	}

	public static boolean isRabinMiller(BigInteger n, int iterations) {
		if(n.compareTo(THREE)<=0) {
			return false;
		}
		if(n.mod(TWO).equals(ZERO)) {
			return false;
		}

		String[] firstBases = {"2047", "1373653", "25326001", "3215031751", "2152302898747", "3474749660383",
				"341550071728321", "341550071728321"};

		BigInteger rndBases[] = new BigInteger[iterations];

		for (int i = 0; i < rndBases.length; i++) {
			if(i<firstBases.length) {
				rndBases[i] = new BigInteger(firstBases[i]);
			}else {
				do
					rndBases[i] = new BigInteger(n.bitLength(), new Random(Long.MAX_VALUE^System.nanoTime()));
				while(rndBases[i].compareTo(n) > 0);
			}
		}

		BigInteger s = n.subtract(ONE);

		int factors = 0;
		while(s.mod(TWO).equals(ZERO)) {
			s = s.divide(TWO);
			factors++;;
		}

		int idx = 0;
		ProbablyPrime:
			for (BigInteger rndBase = rndBases[idx]; idx < iterations; rndBase = rndBases[idx++]) {



				BigInteger x = rndBase.modPow(s,n);
				if(x.equals(ONE)||x.equals(n.subtract(ONE))){
					continue ProbablyPrime;
				}

				for(int j = 1; j<factors; j++) {
					//x = rndBase.modPow(s.multiply(TWO.pow(j)), n);//<-- slow
					x = x.modPow(TWO, n);							//<-- quick
					if(x.equals(ONE)) {
						return false;
					}
					if(x.equals(n.subtract(ONE))) {
						continue ProbablyPrime;
					}
				}
				return false;
			}
		return true;
	}

	//Name: Inverse Mod m
	//Find v such that d=gcd(a,m)=a x v mod m, if d=1 then v is the
	//inverse of a modulo m

	public static BigInteger inverseMod(BigInteger a, BigInteger m) {
		BigInteger q, t2, t3, v1 = ZERO, v2 = ONE, d1 = m, d2 = a;
		while (!d2.equals(ZERO)){				//d2 != 0)
			q = d1.divide(d2);					//d1 div d2;
			t2 = v1.subtract(q.multiply(v2));	//v1 - q*v2;
			t3 = d1.subtract(q.multiply(d2));	//d1 - q*d2;
			v1 = v2; 
			d1 = d2;
			v2 = t2; 
			d2 = t3;
		}
		BigInteger v=v1;
		BigInteger d=d1;
		
		if(d1.compareTo(ONE) > 0){
			return null;
		}
		if(v1.compareTo(ZERO) < 0){
			return v1.add(m);
		}
		else{
			return v1;
		}												// inverse of a modulo m
	}
	
	public static BigInteger randPrimeByBitSize(int bitSize, int iterations) {
		BigInteger test = null;
		Random rand = new Random();
		while(true) {
			test = new BigInteger(bitSize, rand);
			if(isRabinMiller(test, iterations)) {
				return test;
			}
		}
	}
	
	public static BigInteger newRandBigInt(BigInteger min, BigInteger max) {
		BigInteger random;
		do
			random = new BigInteger(max.bitLength(), new Random(Long.MAX_VALUE^System.nanoTime()));
		while(random.compareTo(min) < 0 && random.compareTo(max)>0);
		return random;
		
	}
	
	public static void RSA() {
		BigInteger p, q, N, e, d, s, c, z, temp;
		p = randPrimeByBitSize(512, 20);
		q = randPrimeByBitSize(512, 20);
		N = p.multiply(q);
		temp = p.subtract(ONE).multiply(q.subtract(ONE));
		e = ONE.add(TWO.pow(16));
		d = inverseMod(e,temp);
		s = newRandBigInt(ONE, N);
		c = s.modPow(e, N);
		z = c.modPow(d, N);
		
		System.out.println("p: " + p);
		System.out.println("q: " + q);
		System.out.println("N: " + N);
		System.out.println("temp: " + temp);
		System.out.println("e: " + e);
		System.out.println("d: " + d);
		System.out.println("s: " + s);
		System.out.println("c: " + c);
		System.out.println("z: " + z);
		
		if(s.equals(z)) {
			System.out.println("s==z");
		}else {
			System.out.println("RSA FAIL");
		}
	}
}
