package class019;

// 本文件课上没有讲
// java同学可以使用FastReader进行快读，可以使用FastWriter进行快写，速度是很快的
// 如何使用可以参考main函数
//
// FastReader和FastWriter的作用：
// 1. 提供比Scanner和System.out.println更快的输入输出方式
// 2. 适用于算法竞赛和在线评测系统中需要处理大量数据的场景
// 3. 避免因输入输出效率问题导致的超时
//
// FastReader实现原理：
// 1. 使用InputStream直接读取字节数据
// 2. 内部维护缓冲区，减少系统调用次数
// 3. 手动解析数字字符，避免字符串转换开销
//
// FastWriter实现原理：
// 1. 使用内部缓冲区，批量写入数据
// 2. 手动将数字转换为字符，避免字符串转换开销
// 3. 提供flush机制确保数据正确输出
//
// 时间复杂度：
// - FastReader读取操作：O(1)均摊时间复杂度
// - FastWriter写入操作：O(1)均摊时间复杂度
//
// 空间复杂度：
// - FastReader：O(BUF_SIZE)，其中BUF_SIZE为缓冲区大小
// - FastWriter：O(BUF_SIZE)，其中BUF_SIZE为缓冲区大小

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.InputMismatchException;

public class Code06_FastReaderWriter {

	public static void main(String[] args) {
		FastReader reader = new FastReader(System.in);
		FastWriter writer = new FastWriter(System.out);
		System.out.println("输入一个字符：");
		int cha = reader.readByte(); // reader会读到字符的ASCII码
		System.out.println("输入一个int类型的数字：");
		int num1 = reader.readInt(); // reader会读到该数字
		System.out.println("输入一个long类型的数字：");
		long num2 = reader.readLong(); // reader会读到该数字
		System.out.println("打印结果:");
		writer.println(cha);
		writer.println(num1);
		writer.println(num2);
		writer.close();// close方法包含flush，会把结果刷出去
	}

	// 快读
	// 实现细节：
	// 1. 使用字节数组作为缓冲区，减少系统调用
	// 2. 逐字节读取并解析数字，避免字符串转换
	// 3. 处理负数情况
	// 
	// 适用场景：
	// 1. 算法竞赛中需要快速读取大量数据
	// 2. 在线评测系统中避免输入超时
	// 3. 需要手动控制输入格式的场景
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
	// 实现细节：
	// 1. 使用字节数组作为缓冲区，批量写入数据
	// 2. 手动将数字转换为字符，避免字符串转换开销
	// 3. 提供flush机制确保数据正确输出
	// 
	// 适用场景：
	// 1. 算法竞赛中需要快速输出大量数据
	// 2. 在线评测系统中避免输出超时
	// 3. 需要手动控制输出格式的场景
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