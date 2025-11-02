package class044;

/*
 * 题目1: LeetCode 208. 实现 Trie (前缀树)
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
 * 
 * 题目描述：
 * Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。
 * 这一数据结构有相当多的应用情景，例如自动补全和拼写检查。
 * 请你实现 Trie 类：
 * Trie() 初始化前缀树对象。
 * void insert(String word) 向前缀树中插入字符串 word 。
 * boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
 * boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 1. Trie树是一种专门处理字符串前缀的数据结构
 * 2. 每个节点包含若干子节点（对应不同字符）和一个标记（表示是否为单词结尾）
 * 3. 插入操作：从根节点开始，逐字符查找，若不存在则创建新节点
 * 4. 搜索操作：从根节点开始，逐字符查找，若路径存在且终点为单词结尾则返回true
 * 5. 前缀搜索：从根节点开始，逐字符查找，若路径存在则返回true
 * 
 * 时间复杂度分析：
 * 1. insert操作：O(m)，m为插入字符串的长度
 * 2. search操作：O(m)，m为搜索字符串的长度
 * 3. startsWith操作：O(m)，m为前缀字符串的长度
 * 空间复杂度分析：
 * 1. O(ALPHABET_SIZE * N * M)，其中N是插入的字符串数量，M是字符串的平均长度
 * 2. 最坏情况下，没有公共前缀，每个字符都需要一个节点
 * 是否为最优解：是，这是Trie树的标准实现，时间复杂度已达到理论最优
 * 
 * 工程化考量：
 * 1. 异常处理：可以增加输入参数校验，如检查word是否为null或空字符串
 * 2. 可配置性：可以支持不同的字符集（不仅仅是小写字母a-z）
 * 3. 线程安全：当前实现不是线程安全的，如需线程安全需要额外同步机制
 * 4. 性能优化：可以使用对象池减少频繁创建节点对象的开销
 * 5. 内存优化：对于稀疏字符集，使用哈希表比数组更节省空间
 * 
 * 语言特性差异：
 * 1. Java：使用引用类型，有垃圾回收机制，数组实现固定子节点
 * 2. C++：需要手动管理内存，可以使用数组或指针数组实现
 * 3. Python：动态类型语言，字典实现自然，但性能不如编译型语言
 * 
 * 与机器学习等领域的联系：
 * 1. 自然语言处理：Trie树可用于构建词典、前缀匹配等
 * 2. 信息检索：搜索引擎的自动补全功能常使用Trie树实现
 * 3. 数据压缩：在某些压缩算法中，Trie树用于构建霍夫曼编码树
 * 4. 生物信息学：用于DNA序列匹配和分析
 * 
 * 反直觉但关键的设计：
 * 1. 每个节点不直接存储字符，而是通过父节点到子节点的路径表示字符
 * 2. 根节点不表示任何字符，仅作为起始点
 * 3. 节点的isEnd标记表示从根节点到当前节点的路径是否构成一个完整单词
 * 
 * 极端场景鲁棒性：
 * 1. 空字符串插入：需要特殊处理根节点的end计数
 * 2. 重复字符串：通过end计数区分出现次数
 * 3. 超长字符串：受限于系统内存，但算法本身无长度限制
 * 4. 大量相似前缀：Trie树的优势场景，能有效共享前缀存储空间
 */

// 用固定数组实现前缀树，空间使用是静态的。推荐！
// 测试链接 : https://www.nowcoder.com/practice/7f8a8553ddbf4eaab749ec988726702b
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code02_TrieTree {

	// 如果将来增加了数据量，就改大这个值
	public static int MAXN = 150001;

	public static int[][] tree = new int[MAXN][26];

	public static int[] end = new int[MAXN];

	public static int[] pass = new int[MAXN];

	public static int cnt;

	public static void build() {
		cnt = 1;
	}

	public static void insert(String word) {
		int cur = 1;
		pass[cur]++;
		for (int i = 0, path; i < word.length(); i++) {
			path = word.charAt(i) - 'a';
			if (tree[cur][path] == 0) {
				tree[cur][path] = ++cnt;
			}
			cur = tree[cur][path];
			pass[cur]++;
		}
		end[cur]++;
	}

	public static int search(String word) {
		int cur = 1;
		for (int i = 0, path; i < word.length(); i++) {
			path = word.charAt(i) - 'a';
			if (tree[cur][path] == 0) {
				return 0;
			}
			cur = tree[cur][path];
		}
		return end[cur];
	}

	public static int prefixNumber(String pre) {
		int cur = 1;
		for (int i = 0, path; i < pre.length(); i++) {
			path = pre.charAt(i) - 'a';
			if (tree[cur][path] == 0) {
				return 0;
			}
			cur = tree[cur][path];
		}
		return pass[cur];
	}

	public static void delete(String word) {
		if (search(word) > 0) {
			int cur = 1;
			// 下面这一行代码，讲课的时候没加
			// 本题不会用到pass[1]的信息，所以加不加都可以，不过正确的写法是加上
			pass[cur]--;
			for (int i = 0, path; i < word.length(); i++) {
				path = word.charAt(i) - 'a';
				if (--pass[tree[cur][path]] == 0) {
					tree[cur][path] = 0;
					return;
				}
				cur = tree[cur][path];
			}
			end[cur]--;
		}
	}

	public static void clear() {
		for (int i = 1; i <= cnt; i++) {
			Arrays.fill(tree[i], 0);
			end[i] = 0;
			pass[i] = 0;
		}
	}

	public static int m, op;

	public static String[] splits;

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		String line = null;
		while ((line = in.readLine()) != null) {
			build();
			m = Integer.valueOf(line);
			for (int i = 1; i <= m; i++) {
				splits = in.readLine().split(" ");
				op = Integer.valueOf(splits[0]);
				if (op == 1) {
					insert(splits[1]);
				} else if (op == 2) {
					delete(splits[1]);
				} else if (op == 3) {
					out.println(search(splits[1]) > 0 ? "YES" : "NO");
				} else if (op == 4) {
					out.println(prefixNumber(splits[1]));
				}
			}
			clear();
		}
		out.flush();
		in.close();
		out.close();
	}

}