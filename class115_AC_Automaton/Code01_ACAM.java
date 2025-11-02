package class102;

/**
 * AC自动机（Aho-Corasick Automaton）模板实现 - 优化版
 * 核心功能：给定多个模式串和一个文本串，高效匹配所有模式串在文本串中的出现次数
 * 适用题目：洛谷P5357【模板】AC自动机（二次加强版）
 * 
 * 注意事项：提交时需将类名改为"Main"
 */

/**
 * 算法详解：
 * AC自动机是一种用于多模式字符串匹配的高效算法，由Alfred V. Aho和Margaret J. Corasick于1975年提出
 * 它巧妙地结合了Trie树和KMP算法的思想，实现了在一次扫描中同时匹配多个模式串的功能
 * 
 * 算法核心思想：
 * 1. 构建Trie树：将所有模式串插入到Trie树中，构建高效的字符串前缀检索结构
 * 2. 构建失配指针（fail指针）：类似KMP算法的next数组，当当前节点匹配失败时，快速跳转到最长可能的匹配前缀
 * 3. 构建直通表：优化查询过程，直接记录每个节点在每个字符下的跳转目标，避免重复查找
 * 4. 文本匹配与计数：遍历文本串进行匹配，统计每个节点的匹配次数
 * 5. 利用fail树传递计数：通过遍历fail指针构成的反向树，将子节点的匹配次数累加到父节点
 * 
 * 时间复杂度分析：
 * - 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串的长度
 * - 构建fail指针：O(∑|Pi|)，使用BFS保证每个节点只被处理一次
 * - 文本匹配：O(|T|)，其中T是文本串的长度
 * - 统计结果：O(∑|Pi|)，通过非递归DFS遍历fail树
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度分析：
 * - Trie树存储：O(∑|Pi| × |Σ|)，其中Σ是字符集大小（此处为26个小写字母）
 * - 辅助数组：O(∑|Pi|)，包括fail数组、times数组等
 * 总空间复杂度：O(∑|Pi| × |Σ|)
 * 
 * 算法优化点：
 * 1. 使用数组代替哈希表存储Trie节点，大幅提升访问速度
 * 2. 构建直通表避免重复跳转，实现O(1)时间的字符转移
 * 3. 使用非递归方式遍历fail树，避免Java递归栈溢出
 * 4. 采用链式前向星构建fail树的反向图，提高遍历效率
 * 5. 使用BufferedReader和PrintWriter优化输入输出效率
 * 
 * 经典题目及应用场景：
 * 1. 洛谷P3808【模板】AC自动机（简单版）
 *    链接：https://www.luogu.com.cn/problem/P3808
 *    描述：给定n个模式串和1个文本串，求有多少个模式串在文本串里出现过
 *    解法：基础AC自动机，匹配时标记出现的模式串
 * 
 * 2. 洛谷P3796【模板】AC自动机（加强版）
 *    链接：https://www.luogu.com.cn/problem/P3796
 *    描述：找出最频繁出现的模式串
 *    解法：AC自动机+计数+排序
 * 
 * 3. 洛谷P5357【模板】AC自动机（二次加强版）
 *    链接：https://www.luogu.com.cn/problem/P5357
 *    描述：求每个模式串在文本串中出现的次数
 *    解法：AC自动机+fail树统计
 * 
 * 4. LeetCode 1032. Stream of Characters
 *    链接：https://leetcode.com/problems/stream-of-characters/
 *    描述：设计算法检查字符流的后缀是否是给定单词之一
 *    解法：反转字符串构建AC自动机
 * 
 * 5. POJ 1204 Word Puzzles
 *    链接：http://poj.org/problem?id=1204
 *    描述：在字母矩阵中找出所有给定的单词
 *    解法：AC自动机+多方向搜索
 * 
 * 6. HDU 2222 Keywords Search
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=2222
 *    描述：统计有多少个不同的模式串在文本串中出现
 *    解法：基础AC自动机应用
 * 
 * 7. ZOJ 3430 Detect the Virus
 *    链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827369615
 *    描述：处理压缩后的病毒特征串，检测文本中是否包含病毒
 *    解法：AC自动机+字符串解码
 * 
 * 8. LeetCode 816. 模糊坐标
 *    链接：https://leetcode.com/problems/ambiguous-coordinates/
 *    描述：将字符串分割为可能的坐标形式
 *    解法：回溯算法+AC自动机优化（可选）
 * 
 * 9. Codeforces 963D Frequency of String
 *    链接：https://codeforces.com/problemset/problem/963/D
 *    描述：找出出现恰好k次的最短子串
 *    解法：后缀数组+AC自动机
 * 
 * 10. SPOJ MANDRAKE
 *    链接：https://www.spoj.com/problems/MANDRAKE/
 *    描述：字符串匹配问题
 *    解法：AC自动机应用
 * 
 * 工程化考量：
 * 1. 内存优化：使用预分配的固定大小数组，避免动态扩容开销；针对大规模数据可考虑使用更紧凑的数据结构
 * 2. 性能优化：使用数组而非链表，构建直通表减少跳转次数，优化I/O操作
 * 3. 异常处理：对输入参数进行校验，处理空输入、超长输入等边界情况
 * 4. 多线程安全：在多线程环境下需要添加同步机制或使用线程局部变量
 * 5. 可配置性：参数化字符集大小、最大字符串长度等，提高代码复用性
 * 6. 边界场景处理：
 *    - 空模式串数组
 *    - 空文本串
 *    - 极长模式串
 *    - 重复模式串
 * 7. 跨语言实现差异：
 *    - Java中需注意字符编码问题和递归深度限制
 *    - C++中需注意内存管理和字符串处理
 *    - Python中需注意性能优化和大字符集处理
 * 
 * 与机器学习/深度学习的联系：
 * 1. 特征工程：在NLP任务中用于提取文本特征，构建词汇表和特征向量
 * 2. 自然语言处理：用于命名实体识别、关键词提取、文本分类等任务
 * 3. 信息检索：在搜索引擎中用于快速匹配查询词和文档
 * 4. 安全领域：用于恶意代码检测、垃圾邮件过滤、敏感词过滤
 * 5. 推荐系统：用于分析用户行为序列，进行模式匹配和兴趣发现
 * 
 * 算法调试技巧：
 * 1. 中间过程打印：打印Trie树结构、fail指针指向、匹配过程中的节点跳转
 * 2. 断言验证：使用断言验证关键逻辑，如fail指针构建的正确性
 * 3. 小测试用例：使用简单的测试用例验证算法各阶段的正确性
 * 4. 性能分析：使用性能分析工具定位瓶颈，如I/O操作、内存访问等
 * 
 * 深入学习方向：
 * 1. 算法变体：如双向AC自动机、动态AC自动机等扩展版本
 * 2. 性能调优：针对特定硬件和数据分布的优化技术
 * 3. 高级应用：结合其他算法（如后缀自动机、哈希算法）解决复杂问题
 * 4. 相关算法学习：KMP算法、Trie树、后缀自动机、后缀树等
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * AC自动机实现类
 * 提供多模式字符串匹配功能，支持高效统计每个模式串在文本中的出现次数
 */
public class Code01_ACAM {

	// 常量定义
	// 最大目标字符串数量
	public static final int MAXN = 200001;
	// 所有目标字符串的最大总字符数量
	public static final int MAXS = 200001;
	// 字符集大小（小写字母a-z）
	public static final int CHARSET_SIZE = 26;

	// 核心数据结构
	// end[i]: 记录第i个目标串的结尾节点编号
	public static int[] end = new int[MAXN];
	// tree[u][c]: 节点u在字符c下的子节点编号
	public static int[][] tree = new int[MAXS][CHARSET_SIZE];
	// fail[u]: 节点u的失配指针
	public static int[] fail = new int[MAXS];
	// 节点计数器，记录当前已使用的节点数
	public static int cnt = 0;

	// 题目相关数据
	// times[u]: 记录节点u对应的字符串出现的次数
	public static int[] times = new int[MAXS];

	// 辅助数据结构
	// box: 可复用的整型数组，用作队列或栈
	public static int[] box = new int[MAXS];
	// 链式前向星，用于构建fail指针的反图（fail树）
	public static int[] head = new int[MAXS];
	public static int[] next = new int[MAXS];
	public static int[] to = new int[MAXS];
	public static int edge = 0;
	// visited: 标记节点是否已访问，用于非递归遍历
	public static boolean[] visited = new boolean[MAXS];

	/**
	 * 向AC自动机中插入目标字符串
	 * 时间复杂度：O(|str|)
	 * @param i 目标字符串的索引（从1开始）
	 * @param str 目标字符串
	 * @throws IllegalArgumentException 如果输入参数无效
	 */
	public static void insert(int i, String str) {
		// 边界检查
		if (i <= 0 || i >= MAXN) {
			throw new IllegalArgumentException("目标字符串索引超出范围");
		}
		if (str == null) {
			throw new IllegalArgumentException("目标字符串不能为null");
		}
		if (str.isEmpty()) {
			// 对于空字符串，我们可以特殊处理，但通常AC自动机不处理空模式串
			return;
		}
		
		char[] s = str.toCharArray();
		int u = 0; // 根节点
		for (int j = 0; j < s.length; j++) {
			int c = s[j] - 'a';
			// 字符有效性检查
			if (c < 0 || c >= CHARSET_SIZE) {
				throw new IllegalArgumentException("字符串包含非法字符: " + s[j]);
			}
			// 如果当前字符对应的子节点不存在，则创建新节点
			if (tree[u][c] == 0) {
				tree[u][c] = ++cnt;
				// 边界检查：防止节点数超过预分配的最大值
				if (cnt >= MAXS) {
					throw new IllegalStateException("节点数超过最大限制");
				}
			}
			// 移动到子节点
			u = tree[u][c];
		}
		// 记录第i个目标字符串的结尾节点
		end[i] = u;
	}

	/**
	 * 设置AC自动机的fail指针和直接直通表
	 * 采用BFS算法构建fail指针，同时优化Trie树为自动机
	 * 时间复杂度：O(∑|Pi|)
	 */
	public static void setFail() {
		// 使用box数组作为队列，l为队首指针，r为队尾指针
		int l = 0;
		int r = 0;
		
		// 初始化根节点的直接子节点
		// 根节点的fail指针指向null（这里用0表示）
		for (int i = 0; i < CHARSET_SIZE; i++) {
			if (tree[0][i] > 0) {
				// 根节点的子节点的fail指针指向根节点
				fail[tree[0][i]] = 0;
				// 将子节点加入队列
				box[r++] = tree[0][i];
			}
		}
		
		// BFS构建fail指针
		while (l < r) {
			int u = box[l++]; // 取出队首节点
			
			for (int i = 0; i < CHARSET_SIZE; i++) {
				if (tree[u][i] == 0) {
					// 优化：构建直通表
					// 如果当前节点没有i字符的子节点，则直接指向fail指针节点的i字符子节点
					tree[u][i] = tree[fail[u]][i];
				} else {
					// 当前节点有i字符的子节点
					// 计算子节点的fail指针：当前节点fail指针节点的i字符子节点
					fail[tree[u][i]] = tree[fail[u]][i];
					// 将子节点加入队列
					box[r++] = tree[u][i];
				}
			}
		}
	}

	/**
	 * 主方法：处理输入输出，构建AC自动机并进行匹配
	 * 解决洛谷P5357【模板】AC自动机（二次加强版）问题
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 初始化输入输出流，使用缓冲流提高效率
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		try {
			// 读取模式串数量
			int n = Integer.parseInt(in.readLine());
			
			// 边界检查
			if (n <= 0 || n >= MAXN) {
				throw new IllegalArgumentException("模式串数量超出范围");
			}
			
			// 构建AC自动机：插入所有模式串
			for (int i = 1; i <= n; i++) {
				String pattern = in.readLine();
				if (pattern == null) {
					throw new IOException("读取模式串失败");
				}
				insert(i, pattern);
			}
			
			// 构建fail指针和直通表
			setFail();
			
			// 读取文本串
			String text = in.readLine();
			if (text == null) {
				throw new IOException("读取文本串失败");
			}
			char[] s = text.toCharArray();
			
			// 文本匹配过程
			int u = 0; // 从根节点开始
			for (int i = 0; i < s.length; i++) {
				int c = s[i] - 'a';
				// 字符有效性检查
				if (c >= 0 && c < CHARSET_SIZE) {
					u = tree[u][c]; // 直接通过直通表跳转
					times[u]++; // 增加当前节点的匹配次数
				}
			}
			
			// 构建fail指针的反图（fail树）
			for (int i = 1; i <= cnt; i++) {
				addEdge(fail[i], i);
			}
			
			// 非递归DFS遍历fail树，汇总匹配次数
			f2(0);
			
			// 输出每个模式串的出现次数
			for (int i = 1; i <= n; i++) {
				out.println(times[end[i]]);
			}
		} catch (Exception e) {
			// 异常处理：输出错误信息
			err.println("Error: " + e.getMessage());
		} finally {
			// 确保资源正确关闭
			out.flush();
			out.close();
			in.close();
		}
	}

	/**
	 * 向链式前向星中添加一条边
	 * 用于构建fail指针的反图
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		// 边界检查
		if (u < 0 || u >= MAXS || v < 0 || v >= MAXS) {
			throw new IllegalArgumentException("节点编号超出范围");
		}
		
		// 更新链式前向星
		next[++edge] = head[u];
		head[u] = edge;
		to[edge] = v;
		
		// 边界检查：防止边数超过预分配的最大值
		if (edge >= MAXS) {
			throw new IllegalStateException("边数超过最大限制");
		}
	}

	/**
	 * 递归DFS遍历fail树，汇总子节点的匹配次数
	 * 注意：此方法在Java中对于大规模数据可能导致栈溢出
	 * 因此实际应用中使用非递归版本f2()
	 * 时间复杂度：O(∑|Pi|)
	 * @param u 当前节点编号
	 */
	public static void f1(int u) {
		// 遍历当前节点的所有子节点（在fail树中）
		for (int i = head[u]; i > 0; i = next[i]) {
			int v = to[i];
			// 递归处理子节点
			f1(v);
			// 将子节点的匹配次数累加到当前节点
			times[u] += times[v];
		}
		// 注意：Java默认的栈大小较小，当树的深度较大时会导致StackOverflowError
		// 例如，当模式串为aaaaa...aaa时，fail树会退化为链状结构
		// 对于这种情况，必须使用非递归的DFS方法（f2()）
	}

	/**
	 * 非递归DFS遍历fail树，汇总子节点的匹配次数
	 * 模拟后序遍历，使用栈和访问标记避免递归栈溢出
	 * 时间复杂度：O(∑|Pi|)
	 * 空间复杂度：O(∑|Pi|)
	 * @param u 起始节点编号（通常为根节点0）
	 */
	public static void f2(int u) {
		// 重置访问标记数组
		// 注意：在实际应用中，可能需要先清理visited数组
		// 这里为了简化，假设每次调用f2()时都是处理新的树
		
		// 使用box数组作为栈
		int r = 0;
		box[r++] = u; // 将根节点入栈
		
		// 非递归后序遍历
		while (r > 0) {
			int cur = box[r - 1]; // 查看栈顶元素但不弹出
			
			if (!visited[cur]) {
				// 第一次访问该节点：标记为已访问，并将所有子节点入栈
				visited[cur] = true;
				// 注意：为了保持后序遍历的顺序，需要逆序入栈子节点
				// 但由于我们只需要汇总次数，顺序不影响结果
				for (int i = head[cur]; i > 0; i = next[i]) {
					int v = to[i];
					if (!visited[v]) {
						box[r++] = v;
					}
				}
			} else {
				// 第二次访问该节点：弹出栈顶，并汇总子节点的匹配次数
				r--;
				// 将所有子节点的匹配次数累加到当前节点
				for (int i = head[cur]; i > 0; i = next[i]) {
					times[cur] += times[to[i]];
				}
			}
		}
		
		// 算法说明：
		// 1. 每个节点会被访问两次：第一次是发现节点时，第二次是处理完所有子节点后
		// 2. 通过visited数组标记节点的访问状态
		// 3. 第一次访问时，将所有未访问的子节点入栈
		// 4. 第二次访问时，汇总所有子节点的匹配次数
		// 5. 这种方法避免了递归调用，不会出现栈溢出问题
		// 6. 对于极深的树结构也能高效处理
	}

	// 异常输出流，用于打印错误信息
	private static final PrintStream err = System.err;

}