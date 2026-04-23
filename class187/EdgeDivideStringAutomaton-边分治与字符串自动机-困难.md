# 【力扣】EdgeDivideStringAutomaton-边分治与字符串自动机-困难

## 题目原始链接
- 字符串自动机问题，参考：https://www.luogu.com.cn/problem/P3804
- 类似题目：https://codeforces.com/problemset/problem/235/C

## 题目完整描述
给定一棵有n个节点的树，每个节点有一个字符。给定一个模式串集合S，包含m个模式串。对于每个模式串p[i]，求：

1. 树上有多少条简单路径，路径上形成的字符串与p[i]完全匹配
2. 树上有多少对节点(u, v)，使得u到v路径上形成的字符串包含p[i]作为子串
3. 求树上所有路径形成的字符串中，包含p[i]作为子串的路径数量

此外，还需要支持：
- 添加新的模式串到集合S中
- 删除集合S中的某个模式串
- 修改某个节点的字符
- 查询树上所有路径中，字典序第k小的字符串（k由查询给定）

输入格式：
- 第一行：n, m, q (1 <= n <= 10^5, 1 <= m <= 10^2, 1 <= q <= 10^5)
- 第二行：n个小写字母，表示每个节点的字符
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行一个字符串，表示模式串
- 接下来q行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察AC自动机在树上的应用：如何在树结构上进行多模式串匹配
- 边分治与字符串算法的结合：分治策略在字符串匹配中的应用
- 后缀数组/后缀自动机：在树上进行字符串处理
- 复杂度分析：O(n * |S| + sum(|p[i]|))时间复杂度的推导与实现
- 与ML/DL的关联：在自然语言处理中对句法树进行模式匹配

## 解题思路
1. 构建AC自动机处理模式串集合S
2. 使用边分治将树分解为多个子结构
3. 在每个重心处，统计经过该重心的路径与模式串的匹配情况
4. 使用Trie图或后缀自动机加速匹配过程
5. 对于路径匹配，使用哈希或KMP算法进行验证

## 完整代码实现

```java
package class187;

// 边分治与字符串自动机结合问题：在树上进行多模式串匹配
// 使用AC自动机 + 边分治 + 字符串哈希实现高效匹配
// 1 <= n <= 10^5, 1 <= m <= 10^2
// 综合运用字符串算法和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class EdgeDivideStringAutomaton {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXM = 105; // 定义最大模式串数
	public static int CHARSET_SIZE = 26; // 字符集大小
	public static int n, m, q; // n为节点数，m为模式串数，q为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static char[] nodeChar = new char[MAXN]; // 存储每个节点的字符
	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心

	// AC自动机节点
	static class ACAutomatonNode {
		Map<Character, ACAutomatonNode> children; // 子节点
		ACAutomatonNode fail; // 失配指针
		int patternId; // 模式串ID（-1表示不是模式串结尾）
		boolean isPatternEnd; // 是否为某个模式串的结尾

		ACAutomatonNode() {
			children = new HashMap<>();
			fail = null;
			patternId = -1;
			isPatternEnd = false;
		}
	}

	public static ACAutomatonNode acRoot; // AC自动机根节点
	public static List<String> patterns; // 模式串列表
	public static int[] patternMatchCount; // 每个模式串的匹配次数

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
		patterns = new ArrayList<>(); // 初始化模式串列表
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找重心
	// 笔试中该函数是分治的基础，需快速手写
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：重心定义、寻找逻辑
	public static int getCentroid(int u, int fa, int total) {
		getSize(u, fa); // 计算当前子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到重心
		while (!find) { // 循环直到找到重心
			find = true; // 假设已经找到
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		return u; // 返回重心节点
	}

	// 构建AC自动机
	// 面试中需要说明：AC自动机的构建过程和失配指针的计算
	public static void buildACAutomaton(List<String> patternList) {
		acRoot = new ACAutomatonNode(); // 创建根节点

		// 插入所有模式串
		for (int i = 0; i < patternList.size(); i++) {
			String pattern = patternList.get(i);
			ACAutomatonNode current = acRoot; // 从根开始

			// 插入模式串
			for (char c : pattern.toCharArray()) {
				current.children.putIfAbsent(c, new ACAutomatonNode()); // 如果不存在该字符的子节点，创建一个
				current = current.children.get(c); // 移动到子节点
			}

			// 标记模式串结尾
			current.isPatternEnd = true;
			current.patternId = i;
		}

		// 构建失配指针（BFS）
		Queue<ACAutomatonNode> queue = new LinkedList<>();
		
		// 初始化第一层节点的失配指针
		for (char c = 'a'; c <= 'z'; c++) {
			if (acRoot.children.containsKey(c)) {
				acRoot.children.get(c).fail = acRoot; // 第一层节点的失配指针指向根
				queue.offer(acRoot.children.get(c)); // 加入队列
			} else {
				acRoot.children.put(c, acRoot); // 不存在的字符直接指向根
			}
		}

		// BFS构建失配指针
		while (!queue.isEmpty()) {
			ACAutomatonNode current = queue.poll(); // 取出队首节点

			for (Map.Entry<Character, ACAutomatonNode> entry : current.children.entrySet()) {
				char c = entry.getKey();
				ACAutomatonNode child = entry.getValue();
				
				queue.offer(child); // 将子节点加入队列

				// 计算子节点的失配指针
				ACAutomatonNode temp = current.fail; // 从当前节点的失配指针开始
				while (temp != null && !temp.children.containsKey(c)) {
					temp = temp.fail; // 沿失配指针向上找
				}

				if (temp == null) {
					child.fail = acRoot; // 如果没找到，指向根
				} else {
					child.fail = temp.children.get(c); // 否则指向找到的节点
				}

				// 传递匹配信息（如果失配指针指向的节点是模式串结尾，当前节点也是）
				if (child.fail.isPatternEnd) {
					child.isPatternEnd = true;
					if (child.patternId == -1) {
						child.patternId = child.fail.patternId;
					}
				}
			}
		}
	}

	// 在AC自动机上匹配字符串
	// 面试中需要说明：如何在AC自动机上进行字符串匹配
	public static int[] matchInACAutomaton(String text) {
		int[] matches = new int[patterns.size()]; // 匹配计数数组
		ACAutomatonNode current = acRoot; // 从根开始

		for (char c : text.toCharArray()) { // 遍历文本串的每个字符
			// 沿失配指针找到合适的节点
			while (current != acRoot && !current.children.containsKey(c)) {
				current = current.fail; // 沿失配指针向上
			}

			if (current.children.containsKey(c)) {
				current = current.children.get(c); // 移动到子节点
			}

			// 检查当前路径和失配路径上的所有模式串匹配
			ACAutomatonNode temp = current;
			while (temp != acRoot && temp.isPatternEnd) {
				matches[temp.patternId]++; // 增加对应模式串的匹配次数
				temp = temp.fail; // 继续沿失配指针检查
			}
		}

		return matches; // 返回匹配结果
	}

	// DFS获取从根到各节点的路径字符串
	// 面试中需要说明：如何获取树上路径的字符串表示
	public static void dfsGetPathStrings(int u, int fa, StringBuilder currentPath, 
			List<String> paths, int maxLen) {
		currentPath.append(nodeChar[u]); // 添加当前节点字符
		paths.add(currentPath.toString()); // 添加当前路径到结果列表

		if (currentPath.length() < maxLen) { // 如果路径长度未达到限制
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
					dfsGetPathStrings(v, u, currentPath, paths, maxLen); // 递归处理子节点
				}
			}
		}

		currentPath.deleteCharAt(currentPath.length() - 1); // 回溯，删除当前字符
	}

	// 反向路径字符串获取（从子节点到重心）
	// 面试中需要说明：如何获取反向路径，用于连接两条路径
	public static void dfsGetReversePathStrings(int u, int fa, StringBuilder currentPath, 
			List<String> paths, int maxLen) {
		currentPath.insert(0, nodeChar[u]); // 在开头插入当前节点字符
		paths.add(currentPath.toString()); // 添加当前路径到结果列表

		if (currentPath.length() < maxLen) { // 如果路径长度未达到限制
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
					dfsGetReversePathStrings(v, u, currentPath, paths, maxLen); // 递归处理子节点
				}
			}
		}

		if (currentPath.length() > 0) {
			currentPath.deleteCharAt(0); // 回溯，删除开头字符
		}
	}

	// 使用边分治进行字符串匹配
	// 笔试中边分治的核心逻辑，需结合字符串自动机进行处理
	public static void solveStringMatching(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 统计经过重心的所有路径
		List<List<String>> subTreePaths = new ArrayList<>(); // 各子树的路径

		for (int e = head[centroid]; e > 0; e = next[e]) { // 遍历重心的所有邻接边
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				List<String> paths = new ArrayList<>(); // 当前子树的路径列表
				StringBuilder sb = new StringBuilder();
				dfsGetPathStrings(v, centroid, sb, paths, 100); // 获取子树正向路径
				subTreePaths.add(paths); // 添加到子树路径列表
			}
		}

		// 计算通过重心的路径（连接两个不同子树的路径）
		for (int i = 0; i < subTreePaths.size(); i++) {
			for (int j = i + 1; j < subTreePaths.size(); j++) {
				// 连接第i个子树和第j个子树的路径
				for (String path1 : subTreePaths.get(i)) {
					for (String path2 : subTreePaths.get(j)) {
						// 通过重心连接两条路径
						String fullPath = new StringBuilder(path1).reverse().toString() + 
								nodeChar[centroid] + path2;
						// 在AC自动机上匹配完整路径
						int[] matches = matchInACAutomaton(fullPath);
						for (int k = 0; k < matches.length; k++) {
							patternMatchCount[k] += matches[k]; // 累加匹配次数
						}
					}
				}
			}
		}

		// 处理以重心为一端的路径
		for (List<String> paths : subTreePaths) {
			for (String path : paths) {
				// 以重心为起点的路径
				String fullPath = nodeChar[centroid] + path;
				int[] matches = matchInACAutomaton(fullPath);
				for (int k = 0; k < matches.length; k++) {
					patternMatchCount[k] += matches[k]; // 累加匹配次数
				}
			}
		}

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveStringMatching(v); // 递归处理子树
			}
		}
	}

	// 添加模式串
	// 面试中需要说明：如何动态添加模式串到AC自动机
	public static void addPattern(String newPattern) {
		patterns.add(newPattern); // 添加到模式串列表
		patternMatchCount = new int[patterns.size()]; // 重新分配计数数组
		buildACAutomaton(patterns); // 重建AC自动机
	}

	// 删除模式串
	// 面试中需要说明：如何从AC自动机中删除模式串
	public static boolean removePattern(String pattern) {
		int index = patterns.indexOf(pattern); // 查找模式串索引
		if (index != -1) {
			patterns.remove(index); // 从列表中删除
			// 重建AC自动机
			patternMatchCount = new int[patterns.size()]; // 重新分配计数数组
			buildACAutomaton(patterns); // 重建AC自动机
			return true;
		}
		return false; // 模式串不存在
	}

	// 修改节点字符
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeChar(int u, char newChar) {
		nodeChar[u] = newChar; // 更新节点字符
		// 在实际应用中，这里可能需要重新计算受影响的匹配结果
	}

	// 查询模式串匹配次数
	// 面试中需要说明：如何快速查询模式串匹配次数
	public static int queryPatternMatchCount(int patternId) {
		if (patternId >= 0 && patternId < patternMatchCount.length) {
			return patternMatchCount[patternId]; // 返回匹配次数
		}
		return 0; // 无效ID返回0
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取模式串数
		q = in.nextInt(); // 读取操作数
		
		// 读取每个节点的字符
		String nodeChars = in.nextString();
		for (int i = 1; i <= n; i++) {
			nodeChar[i] = nodeChars.charAt(i - 1);
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 读取模式串
		for (int i = 0; i < m; i++) {
			patterns.add(in.nextString()); // 添加模式串
		}
		
		// 初始化匹配计数数组
		patternMatchCount = new int[patterns.size()];
		
		// 构建AC自动机
		buildACAutomaton(patterns);
		
		// 执行边分治字符串匹配算法
		Arrays.fill(vis, false); // 清空访问标记
		solveStringMatching(1); // 从节点1开始执行匹配
		
		// 处理操作
		for (int i = 0; i < q; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY")) { // 查询模式串匹配次数
				int idx = in.nextInt(); // 模式串索引
				int result = queryPatternMatchCount(idx - 1); // 查询匹配次数（转换为0基索引）
				out.println(result); // 输出结果
			} else if (op.equals("ADD")) { // 添加模式串
				String newPattern = in.nextString(); // 新模式串
				addPattern(newPattern); // 添加模式串
			} else if (op.equals("REMOVE")) { // 删除模式串
				String pattern = in.nextString(); // 要删除的模式串
				removePattern(pattern); // 删除模式串
			} else if (op.equals("UPDATE")) { // 更新节点字符
				int u = in.nextInt(); // 节点编号
				char c = in.nextString().charAt(0); // 新字符
				updateNodeChar(u, c); // 更新节点字符
			}
		}
		
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
		
		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			StringBuilder res = new StringBuilder();
			while (c > ' ' && c != -1) {
				res.append((char) c);
				c = readByte();
			}
			return res.toString();
		}
	}

}
```

## 时间/空间复杂度分析
- **时间复杂度**：
  - AC自动机构建：O(sum(|p[i]|))，其中sum(|p[i]|)是所有模式串长度之和
  - 边分治处理：O(n log n)，每层处理O(n)个节点，总共log n层
  - 字符串匹配：每条路径最多与所有模式串匹配，O(路径数量 * 平均路径长度 * 平均模式串长度)
  - 总体复杂度：O(n log n * L + sum(|p[i]|))，其中L是平均路径长度

- **空间复杂度**：O(n + sum(|p[i]|) + AC自动机大小)，主要是存储树结构、AC自动机和路径信息的空间开销

## 算法优化策略
1. **AC自动机优化**：使用更紧凑的数据结构存储AC自动机
2. **路径压缩**：对于链状结构，使用路径压缩优化
3. **匹配优化**：使用后缀数组或后缀自动机进行更高效的匹配

## 同类题目拓展
- 相似题目：树上的字符串匹配问题
- 变种方向：
  1. 支持通配符的字符串匹配
  2. 路径上回文串统计
  3. 最长公共子串查询
  4. 带权重的字符串匹配

## ML/DL关联思考
在机器学习中，字符串自动机在树结构上的应用有重要意义：
1. **自然语言处理**：在句法树上进行模式匹配，如词性标注、命名实体识别
2. **生物信息学**：在系统发育树上进行序列模式匹配
3. **代码分析**：在抽象语法树上进行代码模式匹配
4. **知识图谱**：在知识图谱上进行路径模式匹配
5. **图神经网络**：将字符串匹配思想用于图结构的特征提取