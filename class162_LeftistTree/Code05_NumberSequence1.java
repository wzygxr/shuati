package class154;

/**
 * 数字序列问题 - Java实现
 * 
 * 【题目来源】
 * 洛谷 P4331 [BOI2004] Sequence 数字序列
 * 题目链接: https://www.luogu.com.cn/problem/P4331
 * 
 * 【题目大意】
 * 给定一个长度为n的数组A，要求构造出一个长度为n的递增数组B
 * 希望 |A[1] - B[1]| + |A[2] - B[2]| + ... + |A[n] - B[n]| 最小
 * 打印这个最小值，然后打印数组B，如果有多个方案，只打印其中的一个
 * 
 * 【数据范围】
 * 1 <= n <= 10^6
 * 0 <= A[i] <= 2^32 - 1
 * 
 * 【算法思路】
 * 使用左偏树维护每个连续段的中位数，结合单调栈优化
 * 通过贪心策略，将原问题转化为维护每个连续段的上中位数
 * 利用左偏树的合并操作和删除操作，动态维护每个段的最优解
 * 
 * 【核心操作】
 * 1. 合并操作(merge): O(log n)时间复杂度
 * 2. 删除堆顶(pop): O(log n)时间复杂度
 * 3. 查找操作(find): 近似O(1)时间复杂度（路径压缩优化）
 * 
 * 【提交说明】
 * 提交时请把类名改成"Main"，一些测试用例通过不了，空间超了
 * 这是洛谷平台没有考虑其他语言导致的，同样的逻辑，C++实现就能完全通过
 * C++实现的版本，就是Code05_NumberSequence2文件
 */

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.InputMismatchException;

public class Code05_NumberSequence1 {

    /**
     * 最大节点数，根据题目约束设置为1000001
     */
    public static int MAXN = 1000001;

    /**
     * 数组长度
     */
    public static int n;

    /**
     * arr[i] 表示处理后的数组元素（A[i] - i）
     */
    public static long[] arr = new long[MAXN];

    /**
     * left[i] 表示节点i的左子节点
     */
    public static int[] left = new int[MAXN];

    /**
     * right[i] 表示节点i的右子节点
     */
    public static int[] right = new int[MAXN];

    /**
     * dist[i] 表示节点i的距离（到最近外节点的边数）
     * 距离定义：节点到其子树中最近的外节点（左子树或右子树为空的节点）的边数
     */
    public static int[] dist = new int[MAXN];

    /**
     * father[i] 表示节点i在并查集中的父节点
     * 用于快速找到堆的根节点
     */
    public static int[] father = new int[MAXN];

    /**
     * from[i] 表示以节点i为根的集合表达区域的左下标
     */
    public static int[] from = new int[MAXN];

    /**
     * to[i] 表示以节点i为根的集合表达区域的右下标
     */
    public static int[] to = new int[MAXN];

    /**
     * size[i] 表示以节点i为根的集合里有几个数字
     */
    public static int[] size = new int[MAXN];

    /**
     * stack[] 单调栈，用于维护递增序列
     */
    public static int[] stack = new int[MAXN];

    /**
     * ans[i] 表示构造的数组B的第i个元素
     */
    public static long[] ans = new long[MAXN];

    /**
     * 初始化函数，设置每个节点的初始状态
     * 为n个节点初始化左偏树和相关数据结构
     * 
     * @timecomplexity O(n) - 遍历每个节点进行初始化
     */
    public static void prepare() {
        // 空节点的距离定义为-1，这是左偏树的基本约定
        dist[0] = -1;
        
        // 初始化每个节点的状态
        for (int i = 1; i <= n; i++) {
            // 每个节点初始时没有左右子节点
            left[i] = right[i] = 0;
            // 每个节点初始时距离为0
            dist[i] = 0;
            // 每个节点初始时自己是自己的代表节点
            father[i] = i;
            // 每个节点初始时表达区域的左右下标都是自己
            from[i] = to[i] = i;
            // 每个节点初始时集合大小为1
            size[i] = 1;
        }
    }

    /**
     * 并查集查找函数，带路径压缩优化
     * 查找节点i所在集合的代表元素（根节点）
     * 
     * @param i 要查找的节点编号
     * @return 节点i所在集合的代表元素
     * @timecomplexity O(α(n)) - 近似常数时间，α是阿克曼函数的反函数
     * @spacecomplexity O(α(n)) - 递归调用栈空间
     */
    public static int find(int i) {
        // 路径压缩优化：递归查找过程中将路径上的所有节点直接连到根节点
        return father[i] = (father[i] == i) ? i : find(father[i]);
    }

    /**
     * 合并两棵左偏树，维护大根堆性质
     * 左偏树合并是其核心操作，通过递归方式将两棵左偏树合并为一棵
     * 
     * @param i 第一棵左偏树的根节点编号
     * @param j 第二棵左偏树的根节点编号
     * @return 合并后新树的根节点编号
     * @timecomplexity O(log n) - 合并操作的时间复杂度与树高相关，由于左偏性质，树高不超过O(log n)
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int merge(int i, int j) {
        // 递归终止条件：如果其中一个节点为空，返回另一个节点
        if (i == 0 || j == 0) {
            return i + j;
        }
        
        // 维护大根堆性质，确保i是根节点值较大的树
        if (arr[i] < arr[j]) {
            // 交换i和j，确保i始终是根节点值更大的树
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并i的右子节点和j
        right[i] = merge(right[i], j);
        
        // 维护左偏性质：左子节点的距离不小于右子节点的距离
        if (dist[left[i]] < dist[right[i]]) {
            // 交换左右子节点以保持左偏性质
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新节点i的距离
        dist[i] = dist[right[i]] + 1;
        
        // 更新子节点的父节点信息
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }

    /**
     * 删除堆顶元素（最大值）
     * 从左偏树中删除最大值节点，并保持左偏树的性质
     * 
     * @param i 堆顶节点编号
     * @return 删除堆顶后新树的根节点编号
     * @timecomplexity O(log n) - 主要开销来自合并左右子树的操作
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int pop(int i) {
        // 将左右子节点的father设置为自己（解除父子关系）
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 合并左右子树，作为新的根
        father[i] = merge(left[i], right[i]);
        
        // 清空节点i的信息
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }

    /**
     * 计算最小绝对值差和，并构造递增数组B
     * 使用左偏树维护每个连续段的中位数，结合单调栈优化
     * 
     * @return 最小绝对值差和
     * @timecomplexity O(n * log n) - 每个元素可能需要多次合并和删除操作
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static long compute() {
        // 单调栈大小
        int stackSize = 0;
        
        // 从左到右处理每个元素
        for (int i = 1, pre, cur, s; i <= n; i++) {
            // 维护单调栈的递增性质
            while (stackSize > 0) {
                // 找到栈顶元素所在集合的根节点
                pre = find(stack[stackSize]);
                // 找到当前元素所在集合的根节点
                cur = find(i);
                
                // 如果栈顶元素的值小于等于当前元素的值，保持单调性
                if (arr[pre] <= arr[cur]) {
                    break;
                }
                
                // 合并两个集合
                s = size[pre] + size[cur];
                cur = merge(pre, cur);
                
                // 大根堆只保留到上中位数
                // 保证合并后的集合大小不超过区间长度的上中位数
                while (s > (i - from[pre] + 1 + 1) / 2) {
                    cur = pop(cur);
                    s--;
                }
                
                // 更新合并后集合的表达区域和大小
                from[cur] = from[pre];
                to[cur] = i;
                size[cur] = s;
                
                // 弹出栈顶元素
                stackSize--;
            }
            
            // 将当前元素压入栈
            stack[++stackSize] = i;
        }
        
        // 计算最小绝对值差和，并构造数组B
        long sum = 0;
        for (int i = 1, cur; i <= stackSize; i++) {
            // 找到栈中第i个元素所在集合的根节点
            cur = find(stack[i]);
            
            // 为该集合表达区域内的所有位置设置相同的值
            for (int j = from[cur]; j <= to[cur]; j++) {
                // 设置数组B的值（需要加上j，因为之前减去了j）
                ans[j] = arr[cur];
                // 累加绝对值差
                sum += Math.abs(ans[j] - arr[j]);
            }
        }
        
        return sum;
    }

    /**
     * 主函数，处理输入输出和操作执行
     * 读取输入数据，初始化数据结构，计算最小值并输出结果
     * 
     * @param args 命令行参数
     * @timecomplexity O(n * log n) - 主要开销来自compute函数
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static void main(String[] args) {
        // 使用自定义快速输入输出类优化IO性能
        FastReader in = new FastReader(System.in);
        FastWriter out = new FastWriter(System.out);
        
        // 读入n
        n = in.readInt();
        
        // 初始化
        prepare();
        
        // 读入数组A，并进行预处理（A[i] - i）
        for (int i = 1; i <= n; i++) {
            arr[i] = in.readLong() - i;
        }
        
        // 计算最小绝对值差和并输出
        out.println(compute());
        
        // 输出构造的递增数组B
        for (int i = 1; i <= n; i++) {
            // 需要加上i，因为之前减去了i
            out.write((ans[i] + i));
            out.write(" ");
        }
        out.writeln();
        
        out.flush();
        out.close();
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
