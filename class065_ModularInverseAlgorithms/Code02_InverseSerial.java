package class099;

// 连续数字逆元的线性递推
// 给定n、p，求1∼n中所有整数在模p意义下的乘法逆元
// 1 <= n <= 3 * 10^6
// n < p < 20000528
// 输入保证p为质数
// 测试链接 : https://www.luogu.com.cn/problem/P3811
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 题目名称: 【模板】乘法逆元
// 题目来源: 洛谷 (Luogu)
// 题目链接: https://www.luogu.com.cn/problem/P3811
// 题目难度: 模板题
//
// 题目描述:
// 给定n,p求1∼n中所有整数在模p意义下的乘法逆元。
// 这里a模p的乘法逆元定义为ax≡1(modp)的解。
//
// 输入格式:
// 一行两个正整数n,p
//
// 输出格式:
// 输出n行，第i行表示i在模p下的乘法逆元
//
// 数据范围:
// 1 <= n <= 3 * 10^6
// n < p < 20000528
// 输入保证p为质数
//
// 解题思路:
// 使用线性递推方法计算所有逆元，时间复杂度O(n)
// 递推公式推导：
// 设 p = k*i + r，其中 k = p // i（整除），r = p % i
// 则有 k*i + r ≡ 0 (mod p)
// 两边同时乘以 i^(-1) * r^(-1) 得：
// k*r^(-1) + i^(-1) ≡ 0 (mod p)
// 即 i^(-1) ≡ -k*r^(-1) (mod p)
// 由于 r < i，所以 r 的逆元在计算 i 的逆元时已经计算过了
//
// 时间复杂度: O(n)
// 空间复杂度: O(n)
//
// 应用场景:
// 1. 批量计算组合数时预处理阶乘逆元
// 2. 数论问题中需要大量模逆元计算
// 3. 密码学中批量生成密钥参数
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.InputMismatchException;

// 如下代码可以通过全部测试用例
// 但是这个题卡常数比较严重
// 一般情况下不会如此卡常数
// 需要使用快读、快写增加IO效率
public class Code02_InverseSerial {

	public static int MAXN = 3000001;

	public static int[] inv = new int[MAXN];

	public static int n, p;

	/**
	 * 使用线性递推方法计算1~n所有整数在模p意义下的乘法逆元
	 * 递推公式推导：
	 * 设 p = k*i + r，其中 k = p / i（整除），r = p % i
	 * 则有 k*i + r ≡ 0 (mod p)
	 * 两边同时乘以 i^(-1) * r^(-1) 得：
	 * k*r^(-1) + i^(-1) ≡ 0 (mod p)
	 * 即 i^(-1) ≡ -k*r^(-1) (mod p)
	 * 由于 r < i，所以 r 的逆元在计算 i 的逆元时已经计算过了
	 * 
	 * 算法优势:
	 * 1. 时间复杂度为O(n)，比逐个计算逆元更高效
	 * 2. 适用于需要批量计算逆元的场景
	 * 3. 避免了多次调用扩展欧几里得算法或快速幂
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * 
	 * @param n 要计算逆元的范围上限
	 */
	public static void build(int n) {
		inv[1] = 1;
		for (int i = 2; i <= n; i++) {
			inv[i] = (int) (p - (long) inv[p % i] * (p / i) % p);
		}
	}

	public static void main(String[] args) {
		FastReader in = new FastReader(System.in);
		FastWriter out = new FastWriter(System.out);
		n = in.readInt();
		p = in.readInt();
		build(n);
		for (int i = 1; i <= n; i++) {
			out.println(inv[i]);
		}
		out.close();
	}
	
	/**
	 * 使用费马小定理计算单个逆元
	 * 当模数p为质数时，a的逆元为a^(p-2) mod p
	 * 
	 * 算法原理:
	 * 根据费马小定理: a^(p-1) ≡ 1 (mod p)
	 * 所以 a^(-1) ≡ a^(p-2) (mod p)
	 * 
	 * 时间复杂度: O(log p)
	 * 空间复杂度: O(1)
	 * 
	 * @param a 要求逆元的数
	 * @param p 质数模数
	 * @return a在模p意义下的逆元
	 */
	public static long modInverseFermat(long a, int p) {
		return power(a, p - 2, p);
	}
	
	/**
	 * 快速幂运算
	 * 计算b^n mod mod
	 * 
	 * 算法原理:
	 * 利用二进制表示指数n，将幂运算分解为若干次平方运算
	 * 例如: 3^10 = 3^8 * 3^2
	 * 
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(1)
	 * 
	 * @param b 底数
	 * @param n 指数
	 * @param mod 模数
	 * @return b^n mod mod
	 */
	public static long power(long b, int n, int mod) {
		long ans = 1;
		while (n > 0) {
			if ((n & 1) == 1) {
				ans = (ans * b) % mod;
			}
			b = (b * b) % mod;
			n >>= 1;
		}
		return ans;
	}
	
	/**
	 * 使用扩展欧几里得算法求模逆元
	 * 适用于模数不一定是质数的情况
	 * 
	 * 算法原理:
	 * 求解方程 ax + by = gcd(a, b)
	 * 当gcd(a, m) = 1时，x就是a的模逆元
	 * 
	 * 时间复杂度: O(log(min(a, mod)))
	 * 空间复杂度: O(1)
	 * 
	 * @param a 要求逆元的数
	 * @param mod 模数
	 * @return 如果存在逆元，返回最小正整数解；否则返回-1
	 */
	public static long modInverseExtendedGcd(long a, int mod) {
		long x = 0, y = 0;
		long gcd = extendedGcd(a, mod, x, y);
		
		// 如果gcd不为1，则逆元不存在
		if (gcd != 1) {
			return -1;
		}
		
		// 确保结果为正数
		return (x % mod + mod) % mod;
	}
	
	/**
	 * 扩展欧几里得算法
	 * 求解 ax + by = gcd(a, b)
	 * 
	 * 算法原理:
	 * 基于欧几里得算法的递归实现
	 * gcd(a, b) = gcd(b, a % b)
	 * 当b = 0时，gcd(a, b) = a
	 * 
	 * 递推关系:
	 * 如果 gcd(a, b) = ax + by
	 * 那么 gcd(b, a % b) = bx' + (a % b)y'
	 * 其中 a % b = a - (a/b)*b
	 * 所以 gcd(a, b) = bx' + (a - (a/b)*b)y' = ay' + b(x' - (a/b)y')
	 * 因此 x = y', y = x' - (a/b)y'
	 * 
	 * 时间复杂度: O(log(min(a, b)))
	 * 空间复杂度: O(log(min(a, b)))（递归栈）
	 * 
	 * @param a 系数a
	 * @param b 系数b
	 * @param x 用于返回x的解
	 * @param y 用于返回y的解
	 * @return gcd(a, b)
	 */
	public static long extendedGcd(long a, int b, long x, long y) {
		// 基本情况
		if (b == 0) {
			x = 1;
			y = 0;
			return a;
		}
		
		// 递归求解
		long x1 = 0, y1 = 0;
		long gcd = extendedGcd(b, (int)(a % b), x1, y1);
		
		// 更新x和y的值
		x = y1;
		y = x1 - (a / b) * y1;
		
		return gcd;
	}

	// 快读
	public static class FastReader {
		InputStream is;
		private byte[] inbuf = new byte[1024];
		public int lenbuf = 0;
		public int ptrbuf = 0;

		public FastReader(final InputStream is) {
			this.is = is;
		}

		public int readByte() {
			if (lenbuf == -1) {
				throw new InputMismatchException();
			}
			if (ptrbuf >= lenbuf) {
				ptrbuf = 0;
				try {
					lenbuf = is.read(inbuf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (lenbuf <= 0) {
					return -1;
				}
			}
			return inbuf[ptrbuf++];
		}

		public int readInt() {
			return (int) readLong();
		}

		public long readLong() {
			long num = 0;
			int b;
			boolean minus = false;
			while ((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'))
				;
			if (b == '-') {
				minus = true;
				b = readByte();
			}

			while (true) {
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
				b = readByte();
			}
		}
	}

	// 快写
	public static class FastWriter {
		private static final int BUF_SIZE = 1 << 13;
		private final byte[] buf = new byte[BUF_SIZE];
		private OutputStream out;
		private Writer writer;
		private int ptr = 0;

		public FastWriter(Writer writer) {
			this.writer = new BufferedWriter(writer);
			out = new ByteArrayOutputStream();
		}

		public FastWriter(OutputStream os) {
			this.out = os;
		}

		public FastWriter(String path) {
			try {
				this.out = new FileOutputStream(path);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("FastWriter");
			}
		}

		public FastWriter write(byte b) {
			buf[ptr++] = b;
			if (ptr == BUF_SIZE) {
				innerflush();
			}
			return this;
		}

		public FastWriter write(String s) {
			s.chars().forEach(c -> {
				buf[ptr++] = (byte) c;
				if (ptr == BUF_SIZE) {
					innerflush();
				}
			});
			return this;
		}

		private static int countDigits(long l) {
			if (l >= 1000000000000000000L) {
				return 19;
			}
			if (l >= 100000000000000000L) {
				return 18;
			}
			if (l >= 10000000000000000L) {
				return 17;
			}
			if (l >= 1000000000000000L) {
				return 16;
			}
			if (l >= 100000000000000L) {
				return 15;
			}
			if (l >= 10000000000000L) {
				return 14;
			}
			if (l >= 1000000000000L) {
				return 13;
			}
			if (l >= 100000000000L) {
				return 12;
			}
			if (l >= 10000000000L) {
				return 11;
			}
			if (l >= 1000000000L) {
				return 10;
			}
			if (l >= 100000000L) {
				return 9;
			}
			if (l >= 10000000L) {
				return 8;
			}
			if (l >= 1000000L) {
				return 7;
			}
			if (l >= 100000L) {
				return 6;
			}
			if (l >= 10000L) {
				return 5;
			}
			if (l >= 1000L) {
				return 4;
			}
			if (l >= 100L) {
				return 3;
			}
			if (l >= 10L) {
				return 2;
			}
			return 1;
		}

		public FastWriter write(long x) {
			if (x == Long.MIN_VALUE) {
				return write("" + x);
			}
			if (ptr + 21 >= BUF_SIZE) {
				innerflush();
			}
			if (x < 0) {
				write((byte) '-');
				x = -x;
			}
			int d = countDigits(x);
			for (int i = ptr + d - 1; i >= ptr; i--) {
				buf[i] = (byte) ('0' + x % 10);
				x /= 10;
			}
			ptr += d;
			return this;
		}

		public FastWriter writeln(long x) {
			return write(x).writeln();
		}

		public FastWriter writeln() {
			return write((byte) '\n');
		}

		private void innerflush() {
			try {
				out.write(buf, 0, ptr);
				ptr = 0;
			} catch (IOException e) {
				throw new RuntimeException("innerflush");
			}
		}

		public void flush() {
			innerflush();
			try {
				if (writer != null) {
					writer.write(((ByteArrayOutputStream) out).toString());
					out = new ByteArrayOutputStream();
					writer.flush();
				} else {
					out.flush();
				}
			} catch (IOException e) {
				throw new RuntimeException("flush");
			}
		}

		public FastWriter println(long x) {
			return writeln(x);
		}

		public void close() {
			flush();
			try {
				out.close();
			} catch (Exception e) {
			}
		}

	}

}