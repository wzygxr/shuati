package class154;

/**
 * 左偏树（Leftist Tree）模板题2 - Java实现（数据量增强版）
 * 
 * 【题目来源】
 * 洛谷 P2713 罗马游戏
 * 题目链接: https://www.luogu.com.cn/problem/P2713
 * 
 * 【题目大意】
 * 依次给定n个非负数字，表示有n个小根堆，每个堆只有一个数
 * 实现如下两种操作，操作一共调用m次
 * M x y : 第x个数字所在的堆和第y个数字所在的堆合并
 *         如果两个数字已经在一个堆或者某个数字已经删除，不进行合并
 * K x   : 打印第x个数字所在堆的最小值，并且在堆里删掉这个最小值
 *         如果第x个数字已经被删除，也就是找不到所在的堆，打印0
 *         若有多个最小值，优先删除编号小的
 * 
 * 【数据范围】
 * 1 <= n <= 10^6
 * 1 <= m <= 10^5
 * 
 * 【算法思路】
 * 使用左偏树维护多个小根堆，支持快速合并和删除最小值操作
 * 结合并查集快速判断两个节点是否在同一个堆中
 * 由于数据量较大，使用自定义的快速输入输出类优化IO性能
 * 
 * 【核心操作】
 * 1. 合并操作(merge): O(log n)时间复杂度
 * 2. 删除堆顶(pop): O(log n)时间复杂度
 * 3. 查找操作(find): 近似O(1)时间复杂度（路径压缩优化）
 * 
 * 【提交说明】
 * 提交时请把类名改成"Main"，可以通过所有测试用例
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

public class Code01_LeftistTree3 {

    /**
     * 最大节点数，根据题目约束设置为1000001
     */
    public static int MAXN = 1000001;

    /**
     * 节点数量n和操作数量m
     */
    public static int n, m;

    /**
     * 左偏树需要的数组
     * num[i] 表示节点i的值
     */
    public static int[] num = new int[MAXN];

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
     * 并查集需要的father数组，用于快速找到树的根节点
     * father[i] 表示节点i在并查集中的父节点
     * 使用路径压缩优化查找效率
     */
    public static int[] father = new int[MAXN];

    /**
     * 初始化函数，设置每个节点的初始状态
     * 为n个节点初始化左偏树和并查集的数据结构
     * 
     * @timecomplexity O(n) - 遍历每个节点进行初始化
     */
    public static void prepare() {
        // 空节点的距离定义为-1，这是左偏树的基本约定
        // 空节点作为递归终止条件，距离为-1确保计算正确性
        dist[0] = -1;
        
        // 初始化每个节点的状态
        for (int i = 1; i <= n; i++) {
            // 每个节点初始时没有左右子节点，子节点指向空节点(0)
            left[i] = right[i] = 0;
            // 每个节点初始时距离为0（叶子节点到自己的距离为0）
            dist[i] = 0;
            // 每个节点初始时自己是自己的代表节点（并查集）
            // 即每个节点自己构成一个独立的堆
            father[i] = i;
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
        // 这样下次查找时可以直接找到根，大大提高后续查找效率
        return father[i] = (father[i] == i) ? i : find(father[i]);
    }

    /**
     * 合并两棵左偏树，维护小根堆性质
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
        // 0表示空节点
        if (i == 0 || j == 0) {
            return i + j; // 当一个为空时，返回另一个非空节点
        }
        
        // 维护小根堆性质，确保i是根节点较小的树
        // 如果值相同，根据题目要求，编号小的做根节点
        if (num[i] > num[j] || (num[i] == num[j] && i > j)) {
            // 交换i和j，确保i始终是根节点更优的树
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并i的右子节点和j
        // 这是左偏树合并的核心策略：总是将另一棵树合并到右子树
        right[i] = merge(right[i], j);
        
        // 维护左偏性质：左子节点的距离不小于右子节点的距离
        // 如果不满足左偏性质，交换左右子节点
        if (dist[left[i]] < dist[right[i]]) {
            // 交换左右子节点以保持左偏性质
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新节点i的距离
        // 节点的距离等于右子节点的距离加1
        // 这确保了左偏树的平衡性质
        dist[i] = dist[right[i]] + 1;
        
        // 更新子节点的父节点信息
        // 确保每个子节点的父指针正确指向其父节点
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }

    /**
     * 删除堆顶元素（最小值）
     * 从左偏树中删除最小值节点，并保持左偏树的性质
     * 
     * @param i 堆顶节点编号（即最小值节点）
     * @return 删除堆顶后新树的根节点编号
     * @timecomplexity O(log n) - 主要开销来自合并左右子树的操作
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int pop(int i) {
        // 将左右子节点的father设置为自己（解除父子关系）
        // 使左右子树成为独立的子树
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 并查集有路径压缩，所以i下方的某个节点x，可能有father[x] = i
        // 现在要删掉i了，所以需要将左右子树合并后的新根作为i的代表节点
        // 这样后续通过x找根时仍然能找到正确的根节点
        father[i] = merge(left[i], right[i]);
        
        // 清空节点i的信息，标记为已删除状态
        // 这是为了防止重复访问和错误操作
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }

    /**
     * 主函数，处理输入输出和操作执行
     * 读取输入数据，初始化左偏树，处理合并和删除堆顶操作
     * 
     * @param args 命令行参数
     * @timecomplexity O(n + m * log n) - 初始化O(n)，每个操作O(log n)
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static void main(String[] args) {
        // 使用自定义快速输入输出类优化IO性能，适应大数据量
        FastReader in = new FastReader(System.in);
        FastWriter out = new FastWriter(System.out);
        
        // 读入n
        n = in.readInt();
        
        // 初始化
        prepare();
        
        // 读入每个节点的初始值
        for (int i = 1; i <= n; i++) {
            num[i] = in.readInt();
        }
        
        // 读入m
        m = in.readInt();
        
        // 处理m个操作
        String op;
        for (int i = 1, x, y; i <= m; i++) {
            // 读取操作类型
            op = in.readString();
            
            // 操作M：合并两个堆
            if (op.equals("M")) {
                x = in.readInt();
                y = in.readInt();
                
                // 如果x或y已经被删除，不进行合并
                if (num[x] != -1 && num[y] != -1) {
                    // 找到x和y所在的堆的根节点
                    int l = find(x);
                    int r = find(y);
                    
                    // 如果不在同一个堆中，进行合并
                    if (l != r) {
                        merge(l, r);
                    }
                }
            } 
            // 操作K：删除堆顶元素
            else {
                x = in.readInt();
                
                // 如果x已经被删除，输出0
                if (num[x] == -1) {
                    out.println(0);
                } else {
                    // 找到x所在堆的根节点
                    int ans = find(x);
                    // 输出根节点的值
                    out.println(num[ans]);
                    // 删除根节点
                    pop(ans);
                    // 标记节点已被删除
                    num[ans] = -1;
                }
            }
        }
        
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

		public String readString() {
			char cur;
			do {
				cur = (char) readByte();
			} while (cur == ' ' || cur == '\n');
			StringBuilder builder = new StringBuilder();
			while (cur != ' ' && cur != '\n') {
				builder.append(cur);
				cur = (char) readByte();
			}
			return builder.toString();
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
