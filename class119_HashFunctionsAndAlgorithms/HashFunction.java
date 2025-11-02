package class106;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.BitSet;
import java.util.Arrays;
import java.util.Collections;

public class HashFunction {

	// 哈希函数实例
	public static class Hash {

		private MessageDigest md;

		// 打印支持哪些哈希算法
		public static void showAlgorithms() {
			for (String algorithm : Security.getAlgorithms("MessageDigest")) {
				System.out.println(algorithm);
			}
		}

		// 用具体算法名字构造实例
		public Hash(String algorithm) {
			try {
				md = MessageDigest.getInstance(algorithm);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		// 输入字符串返回哈希值
		public String hashValue(String input) {
			byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
			BigInteger bigInt = new BigInteger(1, hashInBytes);
			String hashText = bigInt.toString(16);
			return hashText;
		}

	}

	/**
	 * LintCode 128. Hash Function
	 * 题目来源: https://www.lintcode.com/problem/hash-function/description
	 * 
	 * 题目描述:
	 * 在数据结构中，哈希函数是用来将一个字符串（或任何其他类型）转化为小于哈希表大小且大于等于零的整数。
	 * 一个好的哈希函数可以尽可能少地产生冲突。
	 * 一种广泛使用的哈希函数算法是使用数值33，假设任何字符串都是基于33的一个大整数，比如：
	 * hashcode("abcd") = (ascii(a) * 33^3 + ascii(b) * 33^2 + ascii(c) *33 + ascii(d)) % HASH_SIZE
	 *                  = (97* 33^3 + 98 * 33^2 + 99 * 33 +100) % HASH_SIZE
	 *                  = 3595978 % HASH_SIZE
	 * 其中HASH_SIZE表示哈希表的大小(可以假设一个哈希表就是一个索引0 ~ HASH_SIZE-1的数组)。
	 * 给出一个字符串作为key和一个哈希表的大小，返回这个字符串的哈希值。
	 * 
	 * 样例:
	 * 对于key="abcd" 并且 size=100， 返回 78
	 * 
	 * 算法思路:
	 * 使用霍纳法则（Horner's Rule）优化计算，避免大数溢出:
	 * hashcode = (ascii(a) * 33^3 + ascii(b) * 33^2 + ascii(c) *33 + ascii(d)) % HASH_SIZE
	 * 可以转换为:
	 * hashcode = ((((ascii(a) % HASH_SIZE) * 33 + ascii(b)) % HASH_SIZE) * 33 + ascii(c)) % HASH_SIZE) * 33 + ascii(d)) % HASH_SIZE
	 * 
	 * 时间复杂度: O(n)，其中n是字符串的长度
	 * 空间复杂度: O(1)
	 */
	public static int hashCode(char[] key, int HASH_SIZE) {
		long ans = 0;
		for (int i = 0; i < key.length; i++) {
			ans = (ans * 33 + (int) (key[i])) % HASH_SIZE;
		}
		return (int) ans;
	}
	
	/**
	 * LintCode 128. Hash Function (字符串版本)
	 * 
	 * 时间复杂度: O(n)，其中n是字符串的长度
	 * 空间复杂度: O(1)
	 */
	public static int hashCode(String key, int HASH_SIZE) {
		long ans = 0;
		for (int i = 0; i < key.length(); i++) {
			ans = (ans * 33 + (int) (key.charAt(i))) % HASH_SIZE;
		}
		return (int) ans;
	}

	/**
	 * LeetCode 705. Design HashSet (设计哈希集合)
	 * 题目来源: https://leetcode.com/problems/design-hashset/
	 * 
	 * 题目描述:
	 * 不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
	 * 实现 MyHashSet 类：
	 * void add(key) 向哈希集合中插入值 key 。
	 * bool contains(key) 返回哈希集合中是否存在这个值 key 。
	 * void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。
	 * 
	 * 示例:
	 * 输入：
	 * ["MyHashSet", "add", "add", "contains", "contains", "add", "contains", "remove", "contains"]
	 * [[], [1], [2], [1], [3], [2], [2], [2], [2]]
	 * 输出：
	 * [null, null, null, true, false, null, true, null, false]
	 * 
	 * 约束条件:
	 * 0 <= key <= 10^6
	 * 最多调用 10^4 次 add、remove 和 contains
	 * 
	 * 算法思路:
	 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
	 * 当发生哈希冲突时，将元素添加到对应位置的链表中。
	 * 
	 * 时间复杂度: O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
	 * 空间复杂度: O(n)，存储所有元素
	 */
	static class MyHashSet {
		private static final int BASE = 10000;
		private LinkedList<Integer>[] data;

		/** Initialize your data structure here. */
		public MyHashSet() {
			data = new LinkedList[BASE];
			for (int i = 0; i < BASE; ++i) {
				data[i] = new LinkedList<Integer>();
			}
		}

		public void add(int key) {
			int h = hash(key);
			Iterator<Integer> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				if (element == key) {
					return;
				}
			}
			data[h].offerLast(key);
		}

		public void remove(int key) {
			int h = hash(key);
			Iterator<Integer> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				if (element == key) {
					iterator.remove();
					return;
				}
			}
		}

		/** Returns true if this set contains the specified element */
		public boolean contains(int key) {
			int h = hash(key);
			Iterator<Integer> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				if (element == key) {
					return true;
				}
			}
			return false;
		}

		private static int hash(int key) {
			return key % BASE;
		}
	}

	/**
	 * LeetCode 706. Design HashMap (设计哈希映射)
	 * 题目来源: https://leetcode.com/problems/design-hashmap/
	 * 
	 * 题目描述:
	 * 不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
	 * 实现 MyHashMap 类：
	 * MyHashMap() 用空映射初始化对象
	 * void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
	 * int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
	 * void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。
	 * 
	 * 示例:
	 * 输入：
	 * ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
	 * [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
	 * 输出：
	 * [null, null, null, 1, -1, null, 1, null, -1]
	 * 
	 * 约束条件:
	 * 0 <= key, value <= 10^6
	 * 最多调用 10^4 次 put、get 和 remove 方法
	 * 
	 * 算法思路:
	 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
	 * 每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。
	 * 
	 * 时间复杂度: O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
	 * 空间复杂度: O(n)，存储所有元素
	 */
	static class MyHashMap {
		private static final int BASE = 10000;
		private LinkedList<Pair> data[];
		
		private static class Pair {
			private int key;
			private int value;
			
			public Pair(int key, int value) {
				this.key = key;
				this.value = value;
			}
			
			public int getKey() {
				return key;
			}
			
			public int getValue() {
				return value;
			}
			
			public void setValue(int value) {
				this.value = value;
			}
		}

		/** Initialize your data structure here. */
		public MyHashMap() {
			data = new LinkedList[BASE];
			for (int i = 0; i < BASE; ++i) {
				data[i] = new LinkedList<Pair>();
			}
		}
		
		/** value will always be non-negative. */
		public void put(int key, int value) {
			int h = hash(key);
			Iterator<Pair> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.getKey() == key) {
					pair.setValue(value);
					return;
				}
			}
			data[h].offerLast(new Pair(key, value));
		}

		/** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
		public int get(int key) {
			int h = hash(key);
			Iterator<Pair> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.getKey() == key) {
					return pair.getValue();
				}
			}
			return -1;
		}

		/** Removes the mapping of the specified value key if this map contains a mapping for the key */
		public void remove(int key) {
			int h = hash(key);
			Iterator<Pair> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.getKey() == key) {
					iterator.remove();
					return;
				}
			}
		}

		private static int hash(int key) {
			return key % BASE;
		}
	}

	/**
	 * LeetCode 28. Find the Index of the First Occurrence in a String (实现strStr())
	 * 题目来源: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
	 * 
	 * 题目描述:
	 * 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
	 * 如果 needle 不是 haystack 的一部分，则返回 -1。
	 * 
	 * 示例:
	 * 输入：haystack = "sadbutsad", needle = "sad"
	 * 输出：0
	 * 
	 * 输入：haystack = "leetcode", needle = "leeto"
	 * 输出：-1
	 * 
	 * 算法思路:
	 * 使用Rabin-Karp算法（滚动哈希）实现字符串匹配：
	 * 1. 计算needle的哈希值
	 * 2. 在haystack中维护一个长度为needle.length()的滑动窗口，计算其哈希值
	 * 3. 当哈希值相等时，再进行字符串比较确认（避免哈希冲突）
	 * 
	 * 时间复杂度: O(n+m)，其中n是haystack长度，m是needle长度
	 * 空间复杂度: O(1)
	 */
	public static int strStr(String haystack, String needle) {
		if (needle.isEmpty()) return 0;
		if (haystack.length() < needle.length()) return -1;
		
		int base = 256; // 基数
		int mod = 1000000007; // 大质数，用于取模运算
		
		int needleHash = 0;
		int haystackHash = 0;
		int h = 1; // 用于计算最高位的权重
		
		// 计算needle的哈希值和h的值
		for (int i = 0; i < needle.length(); i++) {
			needleHash = (needleHash * base + needle.charAt(i)) % mod;
			if (i < needle.length() - 1) {
				h = (h * base) % mod;
			}
		}
		
		// 计算haystack第一个窗口的哈希值
		for (int i = 0; i < needle.length(); i++) {
			haystackHash = (haystackHash * base + haystack.charAt(i)) % mod;
		}
		
		// 滑动窗口匹配
		for (int i = 0; i <= haystack.length() - needle.length(); i++) {
			// 如果哈希值相等，再进行字符串比较确认
			if (needleHash == haystackHash) {
				if (haystack.substring(i, i + needle.length()).equals(needle)) {
					return i;
				}
			}
			
			// 计算下一个窗口的哈希值
			if (i < haystack.length() - needle.length()) {
				haystackHash = (base * (haystackHash - (haystack.charAt(i) * h) % mod) + haystack.charAt(i + needle.length())) % mod;
				if (haystackHash < 0) {
					haystackHash += mod;
				}
			}
		}
		
		return -1;
	}

	/**
	 * LeetCode 187. Repeated DNA Sequences (重复的DNA序列)
	 * 题目来源: https://leetcode.com/problems/repeated-dna-sequences/
	 * 
	 * 题目描述:
	 * DNA序列由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。
	 * 例如，"ACGAATTCCG" 是一个 DNA序列。
	 * 在研究 DNA 时，识别 DNA 中的重复序列非常有用。
	 * 给定一个表示 DNA序列 的字符串 s，返回所有在 DNA 分子中出现不止一次的长度为 10 的序列(子字符串)。
	 * 可以按任意顺序返回答案。
	 * 
	 * 示例:
	 * 输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
	 * 输出：["AAAAACCCCC","CCCCCAAAAA"]
	 * 
	 * 输入：s = "AAAAAAAAAAAAA"
	 * 输出：["AAAAAAAAAA"]
	 * 
	 * 算法思路:
	 * 使用滚动哈希技术：
	 * 1. 将每个字符映射为数字：A=0, C=1, G=2, T=3
	 * 2. 使用4进制表示长度为10的序列
	 * 3. 滑动窗口遍历所有长度为10的子串，计算其哈希值
	 * 4. 使用哈希表记录每个哈希值出现的次数
	 * 5. 返回出现次数大于1的序列
	 * 
	 * 时间复杂度: O(n)，其中n是DNA序列长度
	 * 空间复杂度: O(n)，存储所有子串的哈希值
	 */
	public static List<String> findRepeatedDnaSequences(String s) {
		List<String> result = new ArrayList<>();
		if (s.length() < 10) return result;
		
		// 字符到数字的映射
		int[] map = new int[256];
		map['A'] = 0;
		map['C'] = 1;
		map['G'] = 2;
		map['T'] = 3;
		
		int base = 4;
		int mod = 1000000007; // 大质数，用于取模运算
		int windowSize = 10;
		
		// 计算base^(windowSize-1) % mod
		long h = 1;
		for (int i = 0; i < windowSize - 1; i++) {
			h = (h * base) % mod;
		}
		
		// 计算第一个窗口的哈希值
		long hash = 0;
		for (int i = 0; i < windowSize; i++) {
			hash = (hash * base + map[s.charAt(i)]) % mod;
		}
		
		// 使用哈希表记录每个哈希值出现的次数
		Map<Long, Integer> hashMap = new HashMap<>();
		hashMap.put(hash, 1);
		
		// 滑动窗口计算后续哈希值
		for (int i = 1; i <= s.length() - windowSize; i++) {
			// 移除最高位字符，添加最低位字符
			hash = (base * (hash - (map[s.charAt(i - 1)] * h) % mod) + map[s.charAt(i + windowSize - 1)]) % mod;
			if (hash < 0) {
				hash += mod;
			}
			
			// 记录哈希值出现次数
			hashMap.put(hash, hashMap.getOrDefault(hash, 0) + 1);
			
			// 如果某个哈希值出现2次，将其对应的子串加入结果集
			if (hashMap.get(hash) == 2) {
				result.add(s.substring(i, i + windowSize));
			}
		}
		
		return result;
	}

	public static List<String> generateStrings(char[] arr, int n) {
		char[] path = new char[n];
		List<String> ans = new ArrayList<>();
		f(arr, 0, n, path, ans);
		return ans;
	}

	public static void f(char[] arr, int i, int n, char[] path, List<String> ans) {
		if (i == n) {
			ans.add(String.valueOf(path));
		} else {
			for (char cha : arr) {
				path[i] = cha;
				f(arr, i + 1, n, path, ans);
			}
		}
	}

	/**
	 * LeetCode 214. Shortest Palindrome (最短回文串)
	 * 题目来源: https://leetcode.com/problems/shortest-palindrome/
	 * 
	 * 题目描述:
	 * 给你一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。
	 * 找到并返回可以用这种方式转换的最短回文串。
	 * 
	 * 示例:
	 * 输入：s = "aacecaaa"
	 * 输出："aaacecaaa"
	 * 
	 * 输入：s = "abcd"
	 * 输出："dcbabcd"
	 * 
	 * 算法思路:
	 * 使用滚动哈希技术找到s的最长前缀回文串:
	 * 1. 计算s的正向哈希和反向哈希
	 * 2. 使用双指针从两端向中间移动，同时比较正向和反向哈希
	 * 3. 当找到最长前缀回文串后，将剩余部分反转并添加到原字符串前面
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static String shortestPalindrome(String s) {
		if (s.length() <= 1) return s;
		
		// 使用滚动哈希找到最长前缀回文串
		int n = s.length();
		long base = 256;
		long mod = 1000000007;
		long forwardHash = 0;
		long backwardHash = 0;
		long power = 1;
		int maxLen = 0;
		
		for (int i = 0; i < n; i++) {
			forwardHash = (forwardHash * base + s.charAt(i)) % mod;
			backwardHash = (backwardHash + s.charAt(i) * power) % mod;
			if (forwardHash == backwardHash) {
				maxLen = i + 1;
			}
			power = (power * base) % mod;
		}
		
		// 将剩余部分反转并添加到前面
		String suffix = s.substring(maxLen);
		StringBuilder reversedSuffix = new StringBuilder(suffix).reverse();
		return reversedSuffix.toString() + s;
	}
	
	/**
	 * 一致性哈希 (Consistent Hashing) 实现
	 * 
	 * 应用场景: 分布式系统中的负载均衡，如分布式缓存、数据库分片等
	 * 算法原理: 将服务器和键都映射到一个虚拟环上，每个键被分配给顺时针方向遇到的第一个服务器
	 * 优势: 当服务器增减时，只需要重新分配少量键，减少数据迁移
	 */
	static class ConsistentHash {
		private final TreeMap<Integer, String> virtualNodes; // 虚拟节点环
		private final int replicas; // 每个真实节点对应的虚拟节点数
		private final List<String> servers; // 真实服务器列表
		
		public ConsistentHash(int replicas) {
			this.replicas = replicas;
			this.virtualNodes = new TreeMap<>();
			this.servers = new ArrayList<>();
		}
		
		// 添加服务器
		public void addServer(String server) {
			servers.add(server);
			// 为每个真实节点创建多个虚拟节点
			for (int i = 0; i < replicas; i++) {
				String virtualNode = server + "#" + i;
				int hash = getHash(virtualNode);
				virtualNodes.put(hash, server);
			}
		}
		
		// 移除服务器
		public void removeServer(String server) {
			servers.remove(server);
			// 移除对应的所有虚拟节点
			for (int i = 0; i < replicas; i++) {
				String virtualNode = server + "#" + i;
				int hash = getHash(virtualNode);
				virtualNodes.remove(hash);
			}
		}
		
		// 获取键对应的服务器
		public String getServer(String key) {
			if (virtualNodes.isEmpty()) {
				return null;
			}
			
			int hash = getHash(key);
			// 找到顺时针方向的第一个服务器
			Map.Entry<Integer, String> entry = virtualNodes.ceilingEntry(hash);
			// 如果没有比当前hash大的节点，则返回环的第一个节点
			if (entry == null) {
				entry = virtualNodes.firstEntry();
			}
			return entry.getValue();
		}
		
		// 哈希函数
		private int getHash(String key) {
			// 使用FNV-1a哈希算法
			final int FNV_32_INIT = 0x811c9dc5;
			final int FNV_32_PRIME = 0x01000193;
			
			int hash = FNV_32_INIT;
			for (int i = 0; i < key.length(); i++) {
				hash ^= key.charAt(i);
				hash *= FNV_32_PRIME;
			}
			return Math.abs(hash);
		}
		
		// 获取服务器列表
		public List<String> getServers() {
			return new ArrayList<>(servers);
		}
	}
	
	/**
	 * 布隆过滤器 (Bloom Filter) 实现
	 * 
	 * 应用场景: 快速判断一个元素是否可能存在于集合中，如垃圾邮件过滤、缓存穿透防护等
	 * 算法原理: 使用多个哈希函数将元素映射到位数组的不同位置，查询时检查所有位置是否都为1
	 * 特点: 存在一定的误判率，但不会漏判；删除元素困难
	 */
	static class BloomFilter {
		private final BitSet bits; // 位数组
		private final int size; // 位数组大小
		private final int[] seeds; // 多个哈希函数的种子
		
		public BloomFilter(int size, int hashFunctions) {
			this.size = size;
			this.bits = new BitSet(size);
			this.seeds = new int[hashFunctions];
			// 初始化哈希函数种子
			for (int i = 0; i < hashFunctions; i++) {
				seeds[i] = i * 100 + 31; // 使用不同的种子
			}
		}
		
		// 添加元素
		public void add(String element) {
			for (int seed : seeds) {
				int hash = getHash(element, seed);
				bits.set(hash);
			}
		}
		
		// 判断元素是否可能存在
		public boolean mightContain(String element) {
			for (int seed : seeds) {
				int hash = getHash(element, seed);
				if (!bits.get(hash)) {
					return false; // 只要有一个位置为0，元素一定不存在
				}
			}
			return true; // 所有位置都为1，元素可能存在
		}
		
		// 哈希函数
		private int getHash(String element, int seed) {
			int hash = 0;
			for (int i = 0; i < element.length(); i++) {
				hash = seed * hash + element.charAt(i);
			}
			return Math.abs(hash % size);
		}
	}
	
	/**
	 * 双重哈希 (Double Hashing) 实现的哈希表
	 * 
	 * 应用场景: 开放寻址法解决哈希冲突
	 * 算法原理: 使用两个哈希函数，当发生冲突时，第二个哈希函数确定探测步长
	 * 优势: 减少聚集现象，提高哈希表性能
	 */
	static class DoubleHashTable<K, V> {
		private static final int DEFAULT_SIZE = 16;
		private static final double LOAD_FACTOR = 0.75;
		
		private Object[] keys;
		private Object[] values;
		private boolean[] occupied;
		private int size;
		
		@SuppressWarnings("unchecked")
		public DoubleHashTable() {
			keys = new Object[DEFAULT_SIZE];
			values = new Object[DEFAULT_SIZE];
			occupied = new boolean[DEFAULT_SIZE];
			size = 0;
		}
		
		// 插入键值对
		@SuppressWarnings("unchecked")
		public void put(K key, V value) {
			if (key == null) throw new IllegalArgumentException("Key cannot be null");
			
			// 检查是否需要扩容
			if ((double) size / keys.length >= LOAD_FACTOR) {
				rehash();
			}
			
			int index = findInsertionIndex(key);
			keys[index] = key;
			values[index] = value;
			if (!occupied[index]) {
				occupied[index] = true;
				size++;
			}
		}
		
		// 获取值
		@SuppressWarnings("unchecked")
		public V get(K key) {
			if (key == null) return null;
			
			int index = findIndex(key);
			return index != -1 ? (V) values[index] : null;
		}
		
		// 删除键值对
		@SuppressWarnings("unchecked")
		public void remove(K key) {
			if (key == null) return;
			
			int index = findIndex(key);
			if (index != -1) {
				keys[index] = null;
				values[index] = null;
				occupied[index] = false;
				size--;
			}
		}
		
		// 查找插入位置
		@SuppressWarnings("unchecked")
		private int findInsertionIndex(K key) {
			int hash1 = hash1(key);
			int hash2 = hash2(key);
			int index = hash1;
			int step = 1;
			
			// 查找空位置或相同的键
			while (occupied[index]) {
				if (key.equals(keys[index])) {
					return index; // 键已存在，返回该位置以更新值
				}
				index = (hash1 + step * hash2) % keys.length;
				step++;
			}
			
			return index;
		}
		
		// 查找键的索引
		@SuppressWarnings("unchecked")
		private int findIndex(K key) {
			int hash1 = hash1(key);
			int hash2 = hash2(key);
			int index = hash1;
			int step = 1;
			
			// 查找键
			while (occupied[index]) {
				if (key.equals(keys[index])) {
					return index; // 找到键
				}
				index = (hash1 + step * hash2) % keys.length;
				step++;
				// 避免无限循环
				if (step > keys.length) {
					break;
				}
			}
			
			return -1; // 未找到键
		}
		
		// 扩容
		@SuppressWarnings("unchecked")
		private void rehash() {
			Object[] oldKeys = keys;
			Object[] oldValues = values;
			boolean[] oldOccupied = occupied;
			
			keys = new Object[keys.length * 2];
			values = new Object[keys.length * 2];
			occupied = new boolean[keys.length * 2];
			size = 0;
			
			// 重新插入所有键值对
			for (int i = 0; i < oldKeys.length; i++) {
				if (oldOccupied[i]) {
					put((K) oldKeys[i], (V) oldValues[i]);
				}
			}
		}
		
		// 第一个哈希函数
		private int hash1(K key) {
			return Math.abs(key.hashCode() % keys.length);
		}
		
		// 第二个哈希函数，用于计算步长
		private int hash2(K key) {
			return 1 + Math.abs(key.hashCode() % (keys.length - 1));
		}
		
		// 获取大小
		public int size() {
			return size;
		}
	}
	
	/**
	 * LeetCode 1. Two Sum (两数之和)
	 * 题目来源: https://leetcode.com/problems/two-sum/
	 * 
	 * 题目描述:
	 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
	 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
	 * 你可以按任意顺序返回答案。
	 * 
	 * 示例:
	 * 输入：nums = [2,7,11,15], target = 9
	 * 输出：[0,1]
	 * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
	 * 
	 * 算法思路:
	 * 使用哈希表存储每个数字及其对应的索引，遍历数组时检查target - nums[i]是否在哈希表中
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static int[] twoSum(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			int complement = target - nums[i];
			if (map.containsKey(complement)) {
				return new int[]{map.get(complement), i};
			}
			map.put(nums[i], i);
		}
		return new int[]{-1, -1};
	}
	
	/**
	 * LeetCode 49. Group Anagrams (字母异位词分组)
	 * 题目来源: https://leetcode.com/problems/group-anagrams/
	 * 
	 * 题目描述:
	 * 给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
	 * 字母异位词是由重新排列源单词的所有字母得到的一个新单词。
	 * 
	 * 示例:
	 * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
	 * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
	 * 
	 * 算法思路:
	 * 使用排序后的字符串作为哈希表的键，将具有相同排序字符串的单词分组
	 * 
	 * 时间复杂度: O(n * k log k)，其中n是字符串数量，k是字符串最大长度
	 * 空间复杂度: O(n * k)
	 */
	public static List<List<String>> groupAnagrams(String[] strs) {
		Map<String, List<String>> map = new HashMap<>();
		for (String str : strs) {
			char[] chars = str.toCharArray();
			Arrays.sort(chars);
			String key = new String(chars);
			map.putIfAbsent(key, new ArrayList<>());
			map.get(key).add(str);
		}
		return new ArrayList<>(map.values());
	}
	
	/**
	 * LeetCode 242. Valid Anagram (有效的字母异位词)
	 * 题目来源: https://leetcode.com/problems/valid-anagram/
	 * 
	 * 题目描述:
	 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
	 * 注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
	 * 
	 * 示例:
	 * 输入: s = "anagram", t = "nagaram"
	 * 输出: true
	 * 
	 * 算法思路:
	 * 使用哈希表统计每个字符出现的次数，然后比较两个字符串的字符频率
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)，因为字符集大小固定为26
	 */
	public static boolean isAnagram(String s, String t) {
		if (s.length() != t.length()) return false;
		
		int[] count = new int[26];
		for (char c : s.toCharArray()) {
			count[c - 'a']++;
		}
		for (char c : t.toCharArray()) {
			count[c - 'a']--;
			if (count[c - 'a'] < 0) return false;
		}
		return true;
	}
	
	/**
	 * LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)
	 * 题目来源: https://leetcode.com/problems/longest-substring-without-repeating-characters/
	 * 
	 * 题目描述:
	 * 给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
	 * 
	 * 示例:
	 * 输入: s = "abcabcbb"
	 * 输出: 3
	 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
	 * 
	 * 算法思路:
	 * 使用滑动窗口和哈希表记录字符最后出现的位置
	 * 当遇到重复字符时，移动窗口左边界到重复字符的下一个位置
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(min(m, n))，其中m是字符集大小
	 */
	public static int lengthOfLongestSubstring(String s) {
		Map<Character, Integer> map = new HashMap<>();
		int maxLength = 0;
		int left = 0;
		
		for (int right = 0; right < s.length(); right++) {
			char c = s.charAt(right);
			if (map.containsKey(c) && map.get(c) >= left) {
				left = map.get(c) + 1;
			}
			map.put(c, right);
			maxLength = Math.max(maxLength, right - left + 1);
		}
		return maxLength;
	}
	
	/**
	 * LeetCode 76. Minimum Window Substring (最小覆盖子串)
	 * 题目来源: https://leetcode.com/problems/minimum-window-substring/
	 * 
	 * 题目描述:
	 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
	 * 
	 * 示例:
	 * 输入：s = "ADOBECODEBANC", t = "ABC"
	 * 输出："BANC"
	 * 解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
	 * 
	 * 算法思路:
	 * 使用滑动窗口和哈希表统计字符频率
	 * 维护一个计数器记录还需要匹配的字符数量
	 * 
	 * 时间复杂度: O(m + n)
	 * 空间复杂度: O(m + n)
	 */
	public static String minWindow(String s, String t) {
		if (s.length() < t.length()) return "";
		
		Map<Character, Integer> target = new HashMap<>();
		Map<Character, Integer> window = new HashMap<>();
		
		// 统计t中字符频率
		for (char c : t.toCharArray()) {
			target.put(c, target.getOrDefault(c, 0) + 1);
		}
		
		int left = 0, right = 0;
		int required = target.size();
		int formed = 0;
		int minLength = Integer.MAX_VALUE;
		int minLeft = 0, minRight = 0;
		
		while (right < s.length()) {
			char c = s.charAt(right);
			window.put(c, window.getOrDefault(c, 0) + 1);
			
			if (target.containsKey(c) && window.get(c).intValue() == target.get(c).intValue()) {
				formed++;
			}
			
			while (left <= right && formed == required) {
				c = s.charAt(left);
				
				if (right - left + 1 < minLength) {
					minLength = right - left + 1;
					minLeft = left;
					minRight = right;
				}
				
				window.put(c, window.get(c) - 1);
				if (target.containsKey(c) && window.get(c) < target.get(c)) {
					formed--;
				}
				left++;
			}
			right++;
		}
		
		return minLength == Integer.MAX_VALUE ? "" : s.substring(minLeft, minRight + 1);
	}
	
	/**
	 * LeetCode 560. Subarray Sum Equals K (和为K的子数组)
	 * 题目来源: https://leetcode.com/problems/subarray-sum-equals-k/
	 * 
	 * 题目描述:
	 * 给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。
	 * 
	 * 示例:
	 * 输入：nums = [1,1,1], k = 2
	 * 输出：2
	 * 
	 * 算法思路:
	 * 使用前缀和和哈希表，记录每个前缀和出现的次数
	 * 当prefixSum - k在哈希表中存在时，说明存在和为k的子数组
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static int subarraySum(int[] nums, int k) {
		Map<Integer, Integer> prefixSumCount = new HashMap<>();
		prefixSumCount.put(0, 1); // 前缀和为0出现1次
		int prefixSum = 0;
		int count = 0;
		
		for (int num : nums) {
			prefixSum += num;
			if (prefixSumCount.containsKey(prefixSum - k)) {
				count += prefixSumCount.get(prefixSum - k);
			}
			prefixSumCount.put(prefixSum, prefixSumCount.getOrDefault(prefixSum, 0) + 1);
		}
		return count;
	}
	
	/**
	 * LeetCode 347. Top K Frequent Elements (前K个高频元素)
	 * 题目来源: https://leetcode.com/problems/top-k-frequent-elements/
	 * 
	 * 题目描述:
	 * 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
	 * 
	 * 示例:
	 * 输入: nums = [1,1,1,2,2,3], k = 2
	 * 输出: [1,2]
	 * 
	 * 算法思路:
	 * 使用哈希表统计频率，然后使用桶排序或优先队列找出前k个高频元素
	 * 
	 * 时间复杂度: O(n log k)
	 * 空间复杂度: O(n)
	 */
	public static int[] topKFrequent(int[] nums, int k) {
		Map<Integer, Integer> frequencyMap = new HashMap<>();
		for (int num : nums) {
			frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
		}
		
		// 使用桶排序
		List<Integer>[] buckets = new List[nums.length + 1];
		for (int num : frequencyMap.keySet()) {
			int frequency = frequencyMap.get(num);
			if (buckets[frequency] == null) {
				buckets[frequency] = new ArrayList<>();
			}
			buckets[frequency].add(num);
		}
		
		List<Integer> result = new ArrayList<>();
		for (int i = buckets.length - 1; i >= 0 && result.size() < k; i--) {
			if (buckets[i] != null) {
				result.addAll(buckets[i]);
			}
		}
		
		return result.stream().mapToInt(Integer::intValue).toArray();
	}
	
	/**
	 * LeetCode 380. Insert Delete GetRandom O(1) (常数时间插入、删除和获取随机元素)
	 * 题目来源: https://leetcode.com/problems/insert-delete-getrandom-o1/
	 * 
	 * 题目描述:
	 * 实现RandomizedSet类：
	 * RandomizedSet() 初始化 RandomizedSet 对象
	 * bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。
	 * bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。
	 * int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。每个元素应该有相同的概率被返回。
	 * 
	 * 算法思路:
	 * 使用哈希表存储值和索引的映射，使用动态数组存储值
	 * 删除时将要删除的元素与最后一个元素交换，然后删除最后一个元素
	 * 
	 * 时间复杂度: O(1) 平均时间复杂度
	 * 空间复杂度: O(n)
	 */
	static class RandomizedSet {
		private Map<Integer, Integer> valueToIndex;
		private List<Integer> values;
		private java.util.Random random;
		
		public RandomizedSet() {
			valueToIndex = new HashMap<>();
			values = new ArrayList<>();
			random = new java.util.Random();
		}
		
		public boolean insert(int val) {
			if (valueToIndex.containsKey(val)) {
				return false;
			}
			valueToIndex.put(val, values.size());
			values.add(val);
			return true;
		}
		
		public boolean remove(int val) {
			if (!valueToIndex.containsKey(val)) {
				return false;
			}
			int index = valueToIndex.get(val);
			int lastElement = values.get(values.size() - 1);
			
			// 将要删除的元素与最后一个元素交换
			values.set(index, lastElement);
			valueToIndex.put(lastElement, index);
			
			// 删除最后一个元素
			values.remove(values.size() - 1);
			valueToIndex.remove(val);
			
			return true;
		}
		
		public int getRandom() {
			return values.get(random.nextInt(values.size()));
		}
	}
	
	/**
	 * LeetCode 146. LRU Cache (LRU缓存)
	 * 题目来源: https://leetcode.com/problems/lru-cache/
	 * 
	 * 题目描述:
	 * 请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
	 * 实现 LRUCache 类：
	 * LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
	 * int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
	 * void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；如果不存在，则向缓存中插入该组 key-value 。
	 * 如果插入操作导致关键字数量超过 capacity ，则应该逐出最久未使用的关键字。
	 * 
	 * 算法思路:
	 * 使用哈希表+双向链表实现
	 * 哈希表提供O(1)的查找，双向链表维护访问顺序
	 * 
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(capacity)
	 */
	static class LRUCache {
		class DLinkedNode {
			int key;
			int value;
			DLinkedNode prev;
			DLinkedNode next;
			
			DLinkedNode() {}
			DLinkedNode(int key, int value) {
				this.key = key;
				this.value = value;
			}
		}
		
		private Map<Integer, DLinkedNode> cache;
		private int size;
		private int capacity;
		private DLinkedNode head, tail;
		
		public LRUCache(int capacity) {
			this.capacity = capacity;
			this.size = 0;
			cache = new HashMap<>();
			head = new DLinkedNode();
			tail = new DLinkedNode();
			head.next = tail;
			tail.prev = head;
		}
		
		public int get(int key) {
			DLinkedNode node = cache.get(key);
			if (node == null) {
				return -1;
			}
			// 移动到头部
			moveToHead(node);
			return node.value;
		}
		
		public void put(int key, int value) {
			DLinkedNode node = cache.get(key);
			if (node == null) {
				DLinkedNode newNode = new DLinkedNode(key, value);
				cache.put(key, newNode);
				addToHead(newNode);
				size++;
				if (size > capacity) {
					DLinkedNode tail = removeTail();
					cache.remove(tail.key);
					size--;
				}
			} else {
				node.value = value;
				moveToHead(node);
			}
		}
		
		private void addToHead(DLinkedNode node) {
			node.prev = head;
			node.next = head.next;
			head.next.prev = node;
			head.next = node;
		}
		
		private void removeNode(DLinkedNode node) {
			node.prev.next = node.next;
			node.next.prev = node.prev;
		}
		
		private void moveToHead(DLinkedNode node) {
			removeNode(node);
			addToHead(node);
		}
		
		private DLinkedNode removeTail() {
			DLinkedNode res = tail.prev;
			removeNode(res);
			return res;
		}
	}

	/**
	 * Codeforces 271D - Good Substrings (好子串)
	 * 题目来源: https://codeforces.com/problemset/problem/271/D
	 * 
	 * 题目描述:
	 * 给定一个字符串s和一个长度为26的字符串bad，bad[i]='1'表示第i个字母是好的，'0'表示坏的。
	 * 一个子串被认为是好的，如果它包含的坏字母数量不超过k。
	 * 计算s中不同好子串的数量。
	 * 
	 * 算法思路:
	 * 使用滚动哈希技术计算所有子串的哈希值，同时统计坏字母数量
	 * 使用哈希集合存储满足条件的子串哈希值
	 * 
	 * 时间复杂度: O(n^2)，其中n是字符串长度
	 * 空间复杂度: O(n^2)
	 */
	public static int countGoodSubstrings(String s, String bad, int k) {
		int n = s.length();
		Set<Long> set = new HashSet<>();
		long base = 131;
		long mod = (long)1e9 + 7;
		
		for (int i = 0; i < n; i++) {
			long hash = 0;
			int badCount = 0;
			
			for (int j = i; j < n; j++) {
				char c = s.charAt(j);
				if (bad.charAt(c - 'a') == '0') {
					badCount++;
				}
				
				if (badCount > k) {
					break;
				}
				
				hash = (hash * base + (c - 'a' + 1)) % mod;
				set.add(hash);
			}
		}
		
		return set.size();
	}

	/**
	 * Codeforces 514C - Watto and Mechanism (瓦托和机制)
	 * 题目来源: https://codeforces.com/problemset/problem/514/C
	 * 
	 * 题目描述:
	 * 给定n个字符串的字典和m个查询字符串。
	 * 对于每个查询字符串，判断是否存在字典中的一个字符串，使得它们长度相同且最多有一个字符不同。
	 * 
	 * 算法思路:
	 * 使用滚动哈希预处理字典中所有字符串的哈希值
	 * 对于每个查询字符串，尝试修改每个位置的字符，检查修改后的哈希值是否在字典中
	 * 
	 * 时间复杂度: O(nL + mL)，其中L是字符串平均长度
	 * 空间复杂度: O(n)
	 */
	public static boolean[] wattoAndMechanism(String[] dictionary, String[] queries) {
		Set<Long> dictHashes = new HashSet<>();
		long base = 131;
		long mod = (long)1e9 + 7;
		
		// 预处理字典字符串的哈希值
		for (String word : dictionary) {
			long hash = 0;
			long power = 1;
			
			for (int i = 0; i < word.length(); i++) {
				hash = (hash * base + (word.charAt(i) - 'a' + 1)) % mod;
				if (i > 0) {
					power = (power * base) % mod;
				}
			}
			dictHashes.add(hash);
		}
		
		boolean[] results = new boolean[queries.length];
		
		for (int idx = 0; idx < queries.length; idx++) {
			String query = queries[idx];
			int n = query.length();
			long[] prefixHash = new long[n + 1];
			long[] suffixHash = new long[n + 1];
			long[] powers = new long[n + 1];
			
			powers[0] = 1;
			for (int i = 1; i <= n; i++) {
				powers[i] = (powers[i - 1] * base) % mod;
			}
			
			// 计算前缀哈希
			for (int i = 0; i < n; i++) {
				prefixHash[i + 1] = (prefixHash[i] * base + (query.charAt(i) - 'a' + 1)) % mod;
			}
			
			// 计算后缀哈希
			for (int i = n - 1; i >= 0; i--) {
				suffixHash[i] = (suffixHash[i + 1] + (query.charAt(i) - 'a' + 1) * powers[n - i - 1]) % mod;
			}
			
			boolean found = false;
			
			// 尝试修改每个位置的字符
			for (int i = 0; i < n && !found; i++) {
				for (char c = 'a'; c <= 'c'; c++) {
					if (c == query.charAt(i)) continue;
					
					// 计算修改后的哈希值
					long newHash = (prefixHash[i] * powers[n - i] + 
								  (c - 'a' + 1) * powers[n - i - 1] + 
								  suffixHash[i + 1]) % mod;
					
					if (dictHashes.contains(newHash)) {
						found = true;
						break;
					}
				}
			}
			
			results[idx] = found;
		}
		
		return results;
	}

	/**
	 * Codeforces 835D - Palindromic characteristics (回文特性)
	 * 题目来源: https://codeforces.com/problemset/problem/835/D
	 * 
	 * 题目描述:
	 * 定义k级回文串：1级回文串是普通回文串，k级回文串是回文串且前半部分(去掉中间字符)是(k-1)级回文串。
	 * 给定字符串s，对于k=1到n，统计s中有多少个子串是k级回文串。
	 * 
	 * 算法思路:
	 * 使用滚动哈希判断回文串，同时使用动态规划计算回文级别
	 * 
	 * 时间复杂度: O(n^2)
	 * 空间复杂度: O(n^2)
	 */
	public static int[] palindromicCharacteristics(String s) {
		int n = s.length();
		long base = 131;
		long mod = (long)1e9 + 7;
		
		long[] prefixHash = new long[n + 1];
		long[] suffixHash = new long[n + 1];
		long[] powers = new long[n + 1];
		
		powers[0] = 1;
		for (int i = 1; i <= n; i++) {
			powers[i] = (powers[i - 1] * base) % mod;
		}
		
		// 计算前缀哈希
		for (int i = 0; i < n; i++) {
			prefixHash[i + 1] = (prefixHash[i] * base + (s.charAt(i) - 'a' + 1)) % mod;
		}
		
		// 计算后缀哈希
		for (int i = n - 1; i >= 0; i--) {
			suffixHash[i] = (suffixHash[i + 1] + (s.charAt(i) - 'a' + 1) * powers[n - i - 1]) % mod;
		}
		
		int[][] dp = new int[n][n];
		int[] result = new int[n + 1];
		
		for (int len = 1; len <= n; len++) {
			for (int i = 0; i + len <= n; i++) {
				int j = i + len - 1;
				
				// 计算子串s[i..j]的哈希值
				long forwardHash = (prefixHash[j + 1] - prefixHash[i] * powers[len] % mod + mod) % mod;
				long backwardHash = (suffixHash[i] - suffixHash[j + 1] * powers[len] % mod + mod) % mod;
				
				if (forwardHash == backwardHash) {
					dp[i][j] = 1;
					
					// 检查前半部分
					int halfLen = len / 2;
					if (halfLen > 0) {
						int mid = i + halfLen - 1;
						if (dp[i][mid] > 0) {
							dp[i][j] = dp[i][mid] + 1;
						}
					}
					
					for (int k = 1; k <= dp[i][j]; k++) {
						result[k]++;
					}
				}
			}
		}
		
		return Arrays.copyOfRange(result, 1, n + 1);
	}

	/**
	 * 完美哈希 (Perfect Hashing) 实现 - 二级哈希表
	 * 
	 * 应用场景: 静态数据集，需要O(1)查找时间且无冲突
	 * 算法原理: 使用两级哈希表，第一级哈希将元素分组，第二级为每个组创建无冲突的哈希表
	 * 优势: 保证O(1)查找时间，无哈希冲突
	 * 限制: 仅适用于静态数据集，构建过程较复杂
	 */
	static class PerfectHashTable<K, V> {
		private static class SecondaryTable<K, V> {
			private Object[] keys;
			private Object[] values;
			private int size;
			
			public SecondaryTable(int size) {
				this.size = size;
				keys = new Object[size];
				values = new Object[size];
			}
			
			@SuppressWarnings("unchecked")
			public void put(K key, V value, int hash) {
				int index = Math.abs(hash % size);
				keys[index] = key;
				values[index] = value;
			}
			
			@SuppressWarnings("unchecked")
			public V get(K key, int hash) {
				int index = Math.abs(hash % size);
				if (keys[index] != null && keys[index].equals(key)) {
					return (V) values[index];
				}
				return null;
			}
		}
		
		private SecondaryTable<K, V>[] primaryTable;
		private int primarySize;
		private int[] hashSeeds;
		
		@SuppressWarnings("unchecked")
		public PerfectHashTable(List<Pair<K, V>> data) {
			// 使用平方和法确定主表大小
			primarySize = (int) Math.ceil(Math.sqrt(data.size()));
			primaryTable = new SecondaryTable[primarySize];
			hashSeeds = new int[primarySize];
			
			// 初始化主表
			List<List<Pair<K, V>>> buckets = new ArrayList<>();
			for (int i = 0; i < primarySize; i++) {
				buckets.add(new ArrayList<>());
			}
			
			// 第一级哈希分组
			for (Pair<K, V> pair : data) {
				int primaryHash = Math.abs(pair.getKey().hashCode() % primarySize);
				buckets.get(primaryHash).add(pair);
			}
			
			// 为每个桶创建二级哈希表
			for (int i = 0; i < primarySize; i++) {
				List<Pair<K, V>> bucket = buckets.get(i);
				if (!bucket.isEmpty()) {
					// 为二级表寻找无冲突的哈希种子
					int seed = findPerfectSeed(bucket);
					hashSeeds[i] = seed;
					
					// 创建二级哈希表
					int secondarySize = bucket.size() * bucket.size(); // 平方大小保证无冲突
					primaryTable[i] = new SecondaryTable<>(secondarySize);
					
					// 插入数据到二级表
					for (Pair<K, V> pair : bucket) {
						int secondaryHash = Math.abs((pair.getKey().hashCode() ^ seed) % secondarySize);
						primaryTable[i].put(pair.getKey(), pair.getValue(), secondaryHash);
					}
				}
			}
		}
		
		private int findPerfectSeed(List<Pair<K, V>> bucket) {
			int size = bucket.size() * bucket.size();
			int seed = 1;
			
			// 尝试不同的种子直到找到无冲突的
			while (true) {
				Set<Integer> hashes = new HashSet<>();
				boolean collision = false;
				
				for (Pair<K, V> pair : bucket) {
					int hash = Math.abs((pair.getKey().hashCode() ^ seed) % size);
					if (!hashes.add(hash)) {
						collision = true;
						break;
					}
				}
				
				if (!collision) {
					return seed;
				}
				
				seed++;
				if (seed > 1000) { // 防止无限循环
					throw new RuntimeException("Cannot find perfect hash seed");
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		public V get(K key) {
			int primaryHash = Math.abs(key.hashCode() % primarySize);
			if (primaryTable[primaryHash] == null) {
				return null;
			}
			
			int secondaryHash = Math.abs((key.hashCode() ^ hashSeeds[primaryHash]) % 
					(primaryTable[primaryHash].keys.length));
			return primaryTable[primaryHash].get(key, secondaryHash);
		}
	}

	/**
	 * 计数布隆过滤器 (Counting Bloom Filter) 实现
	 * 
	 * 应用场景: 需要支持删除操作的布隆过滤器变种
	 * 算法原理: 使用计数器数组代替位数组，支持元素的添加和删除
	 * 优势: 支持删除操作，保持布隆过滤器的空间效率
	 * 限制: 空间使用略高于标准布隆过滤器
	 */
	static class CountingBloomFilter {
		private final int[] counters; // 计数器数组
		private final int size; // 数组大小
		private final int[] seeds; // 多个哈希函数的种子
		
		public CountingBloomFilter(int size, int hashFunctions) {
			this.size = size;
			this.counters = new int[size];
			this.seeds = new int[hashFunctions];
			// 初始化哈希函数种子
			for (int i = 0; i < hashFunctions; i++) {
				seeds[i] = i * 100 + 31; // 使用不同的种子
			}
		}
		
		// 添加元素
		public void add(String element) {
			for (int seed : seeds) {
				int hash = getHash(element, seed);
				counters[hash]++;
			}
		}
		
		// 删除元素
		public void remove(String element) {
			for (int seed : seeds) {
				int hash = getHash(element, seed);
				if (counters[hash] > 0) {
					counters[hash]--;
				}
			}
		}
		
		// 判断元素是否可能存在
		public boolean mightContain(String element) {
			for (int seed : seeds) {
				int hash = getHash(element, seed);
				if (counters[hash] == 0) {
					return false; // 只要有一个位置为0，元素一定不存在
				}
			}
			return true; // 所有位置都大于0，元素可能存在
		}
		
		// 获取元素计数（近似值）
		public int getCount(String element) {
			int minCount = Integer.MAX_VALUE;
			for (int seed : seeds) {
				int hash = getHash(element, seed);
				minCount = Math.min(minCount, counters[hash]);
			}
			return minCount;
		}
		
		// 哈希函数
		private int getHash(String element, int seed) {
			int hash = 0;
			for (int i = 0; i < element.length(); i++) {
				hash = seed * hash + element.charAt(i);
			}
			return Math.abs(hash % size);
		}
	}

	/**
	 * 可扩展哈希 (Extendible Hashing) 实现
	 * 
	 * 应用场景: 数据库索引、文件系统等需要动态扩展的哈希结构
	 * 算法原理: 使用目录结构指向桶，当桶满时进行分裂，目录深度动态调整
	 * 优势: 支持动态扩展，保持较好的性能
	 * 限制: 目录结构增加了一定的空间开销
	 */
	static class ExtendibleHashTable<K, V> {
		private static class Bucket<K, V> {
			private static final int BUCKET_SIZE = 4; // 桶容量
			private List<Pair<K, V>> entries;
			private int localDepth;
			
			public Bucket(int localDepth) {
				this.entries = new ArrayList<>();
				this.localDepth = localDepth;
			}
			
			public boolean isFull() {
				return entries.size() >= BUCKET_SIZE;
			}
			
			public void add(K key, V value) {
				entries.add(new Pair<>(key, value));
			}
			
			public V get(K key) {
				for (Pair<K, V> entry : entries) {
					if (entry.getKey().equals(key)) {
						return entry.getValue();
					}
				}
				return null;
			}
			
			public void remove(K key) {
				entries.removeIf(entry -> entry.getKey().equals(key));
			}
			
			public List<Pair<K, V>> getEntries() {
				return new ArrayList<>(entries);
			}
		}
		
		private List<Bucket<K, V>> directory;
		private int globalDepth;
		
		@SuppressWarnings("unchecked")
		public ExtendibleHashTable() {
			this.globalDepth = 1;
			this.directory = new ArrayList<>();
			// 初始化目录
			directory.add(new Bucket<>(1));
			directory.add(new Bucket<>(1));
		}
		
		private int getDirectoryIndex(K key) {
			int hash = key.hashCode();
			// 取哈希值的低globalDepth位
			return hash & ((1 << globalDepth) - 1);
		}
		
		public void put(K key, V value) {
			int index = getDirectoryIndex(key);
			Bucket<K, V> bucket = directory.get(index);
			
			if (!bucket.isFull()) {
				bucket.add(key, value);
			} else {
				// 桶满，需要分裂
				splitBucket(index);
				// 重新插入
				put(key, value);
			}
		}
		
		@SuppressWarnings("unchecked")
		private void splitBucket(int index) {
			Bucket<K, V> oldBucket = directory.get(index);
			
			if (oldBucket.localDepth == globalDepth) {
				// 需要扩展目录
				extendDirectory();
			}
			
			// 创建新桶
			Bucket<K, V> newBucket = new Bucket<>(oldBucket.localDepth + 1);
			oldBucket.localDepth++;
			
			// 重新分配条目
			List<Pair<K, V>> entries = oldBucket.getEntries();
			oldBucket.entries.clear();
			
			for (Pair<K, V> entry : entries) {
				int newIndex = getDirectoryIndex(entry.getKey());
				if ((newIndex & (1 << (oldBucket.localDepth - 1))) != 0) {
					newBucket.add(entry.getKey(), entry.getValue());
				} else {
					oldBucket.add(entry.getKey(), entry.getValue());
				}
			}
			
			// 更新目录指针
			for (int i = 0; i < directory.size(); i++) {
				if ((i & ((1 << globalDepth) - 1)) == index) {
					if ((i & (1 << (oldBucket.localDepth - 1))) != 0) {
						directory.set(i, newBucket);
					} else {
						directory.set(i, oldBucket);
					}
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		private void extendDirectory() {
			int oldSize = directory.size();
			globalDepth++;
			
			// 扩展目录
			for (int i = 0; i < oldSize; i++) {
				Bucket<K, V> bucket = directory.get(i);
				directory.add(bucket);
			}
		}
		
		public V get(K key) {
			int index = getDirectoryIndex(key);
			Bucket<K, V> bucket = directory.get(index);
			return bucket.get(key);
		}
		
		public void remove(K key) {
			int index = getDirectoryIndex(key);
			Bucket<K, V> bucket = directory.get(index);
			bucket.remove(key);
			
			// TODO: 实现桶合并逻辑
		}
	}

	/**
	 * 线性哈希 (Linear Hashing) 实现
	 * 
	 * 应用场景: 数据库系统、文件系统等需要渐进式扩展的哈希结构
	 * 算法原理: 使用线性探测和分裂策略，避免目录结构的空间开销
	 * 优势: 渐进式扩展，无目录结构开销
	 * 限制: 分裂过程可能影响性能
	 */
	static class LinearHashTable<K, V> {
		private static final int INITIAL_SIZE = 4;
		private static final double LOAD_FACTOR = 0.75;
		
		private List<Bucket<K, V>> buckets;
		private int nextSplit;
		private int level;
		private int size;
		
		private static class Bucket<K, V> {
			private List<Pair<K, V>> entries;
			
			public Bucket() {
				this.entries = new ArrayList<>();
			}
			
			public void add(K key, V value) {
				entries.add(new Pair<>(key, value));
			}
			
			public V get(K key) {
				for (Pair<K, V> entry : entries) {
					if (entry.getKey().equals(key)) {
						return entry.getValue();
					}
				}
				return null;
			}
			
			public void remove(K key) {
				entries.removeIf(entry -> entry.getKey().equals(key));
			}
			
			public List<Pair<K, V>> getEntries() {
				return new ArrayList<>(entries);
			}
		}
		
		@SuppressWarnings("unchecked")
		public LinearHashTable() {
			this.buckets = new ArrayList<>();
			this.nextSplit = 0;
			this.level = 0;
			this.size = 0;
			
			// 初始化桶
			for (int i = 0; i < INITIAL_SIZE; i++) {
				buckets.add(new Bucket<>());
			}
		}
		
		private int hash(K key, int level) {
			int hash = key.hashCode();
			int mask = (1 << level) - 1;
			return hash & mask;
		}
		
		public void put(K key, V value) {
			int index = getBucketIndex(key);
			Bucket<K, V> bucket = buckets.get(index);
			
			// 检查是否已存在
			V existing = bucket.get(key);
			if (existing != null) {
				// 更新值
				bucket.remove(key);
				bucket.add(key, value);
				return;
			}
			
			bucket.add(key, value);
			size++;
			
			// 检查是否需要分裂
			if ((double) size / buckets.size() > LOAD_FACTOR) {
				split();
			}
		}
		
		private int getBucketIndex(K key) {
			int hash1 = hash(key, level);
			int hash2 = hash(key, level + 1);
			
			if (hash1 < nextSplit) {
				return hash2;
			} else {
				return hash1;
			}
		}
		
		@SuppressWarnings("unchecked")
		private void split() {
			if (nextSplit >= (1 << level)) {
				// 进入下一层
				level++;
				nextSplit = 0;
			}
			
			// 分裂桶
			Bucket<K, V> oldBucket = buckets.get(nextSplit);
			Bucket<K, V> newBucket = new Bucket<>();
			
			// 重新分配条目
			List<Pair<K, V>> entries = oldBucket.getEntries();
			oldBucket.entries.clear();
			
			for (Pair<K, V> entry : entries) {
				int newIndex = hash(entry.getKey(), level + 1);
				if (newIndex == nextSplit + (1 << level)) {
					newBucket.add(entry.getKey(), entry.getValue());
				} else {
					oldBucket.add(entry.getKey(), entry.getValue());
				}
			}
			
			// 添加新桶
			buckets.add(newBucket);
			nextSplit++;
		}
		
		public V get(K key) {
			int index = getBucketIndex(key);
			Bucket<K, V> bucket = buckets.get(index);
			return bucket.get(key);
		}
		
		public void remove(K key) {
			int index = getBucketIndex(key);
			Bucket<K, V> bucket = buckets.get(index);
			V value = bucket.get(key);
			if (value != null) {
				bucket.remove(key);
				size--;
			}
		}
	}

	/**
	 * 剑指Offer 50. 第一个只出现一次的字符
	 * 题目来源: https://leetcode.cn/problems/di-yi-ge-zhi-chu-xian-yi-ci-de-zi-fu-lcof/
	 * 
	 * 题目描述:
	 * 在字符串 s 中找出第一个只出现一次的字符。如果没有，返回一个单空格。
	 * 
	 * 算法思路:
	 * 使用哈希表统计每个字符出现的次数
	 * 再次遍历字符串找到第一个出现次数为1的字符
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)，因为字符集大小固定
	 */
	public static char firstUniqChar(String s) {
		int[] count = new int[256]; // ASCII字符集
		
		for (char c : s.toCharArray()) {
			count[c]++;
		}
		
		for (char c : s.toCharArray()) {
			if (count[c] == 1) {
				return c;
			}
		}
		
		return ' ';
	}

	/**
	 * 剑指Offer 03. 数组中重复的数字
	 * 题目来源: https://leetcode.cn/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof/
	 * 
	 * 题目描述:
	 * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。
	 * 数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。
	 * 请找出数组中任意一个重复的数字。
	 * 
	 * 算法思路:
	 * 使用哈希集合记录已经出现过的数字
	 * 当遇到已经在集合中的数字时返回
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static int findRepeatNumber(int[] nums) {
		Set<Integer> set = new HashSet<>();
		
		for (int num : nums) {
			if (set.contains(num)) {
				return num;
			}
			set.add(num);
		}
		
		return -1;
	}

	/**
	 * 剑指Offer 48. 最长不含重复字符的子字符串
	 * 题目来源: https://leetcode.cn/problems/zui-chang-bu-han-zhong-fu-zi-fu-de-zi-zi-fu-chuan-lcof/
	 * 
	 * 题目描述:
	 * 请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度。
	 * 
	 * 算法思路:
	 * 使用滑动窗口和哈希表记录字符最后出现的位置
	 * 当遇到重复字符时移动窗口左边界
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)，字符集大小固定
	 */
	public static int lengthOfLongestSubstring2(String s) {
		Map<Character, Integer> map = new HashMap<>();
		int maxLength = 0;
		int left = 0;
		
		for (int right = 0; right < s.length(); right++) {
			char c = s.charAt(right);
			if (map.containsKey(c) && map.get(c) >= left) {
				left = map.get(c) + 1;
			}
			map.put(c, right);
			maxLength = Math.max(maxLength, right - left + 1);
		}
		
		return maxLength;
	}

	/**
	 * HDU 4821 - String (字符串)
	 * 题目来源: http://acm.hdu.edu.cn/showproblem.php?pid=4821
	 * 
	 * 题目描述:
	 * 给定字符串s和整数M,L，统计有多少个长度为M*L的子串，使得该子串可以分成M个长度为L的不同子串。
	 * 
	 * 算法思路:
	 * 使用滚动哈希计算所有长度为L的子串哈希值
	 * 使用滑动窗口统计长度为M*L的窗口中不同子串的数量
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static int countValidSubstrings(String s, int M, int L) {
		int n = s.length();
		if (n < M * L) return 0;
		
		long base = 131;
		long mod = (long)1e9 + 7;
		
		// 计算所有长度为L的子串哈希值
		long[] hashes = new long[n - L + 1];
		long power = 1;
		
		for (int i = 0; i < L; i++) {
			power = (power * base) % mod;
		}
		
		long hash = 0;
		for (int i = 0; i < n; i++) {
			hash = (hash * base + (s.charAt(i) - 'a' + 1)) % mod;
			if (i >= L) {
				hash = (hash - (s.charAt(i - L) - 'a' + 1) * power % mod + mod) % mod;
			}
			if (i >= L - 1) {
				hashes[i - L + 1] = hash;
			}
		}
		
		int count = 0;
		
		for (int start = 0; start < L; start++) {
			if (start + M * L > n) break;
			
			Map<Long, Integer> freq = new HashMap<>();
			
			// 初始化第一个窗口
			for (int i = 0; i < M; i++) {
				int pos = start + i * L;
				long h = hashes[pos];
				freq.put(h, freq.getOrDefault(h, 0) + 1);
			}
			
			if (freq.size() == M) {
				count++;
			}
			
			// 滑动窗口
			for (int i = start + L; i + M * L <= n; i += L) {
				// 移除最左边的子串
				long removeHash = hashes[i - L];
				freq.put(removeHash, freq.get(removeHash) - 1);
				if (freq.get(removeHash) == 0) {
					freq.remove(removeHash);
				}
				
				// 添加最右边的子串
				long addHash = hashes[i + (M - 1) * L];
				freq.put(addHash, freq.getOrDefault(addHash, 0) + 1);
				
				if (freq.size() == M) {
					count++;
				}
			}
		}
		
		return count;
	}

	/**
	 * POJ 2774 - Long Long Message (最长公共子串)
	 * 题目来源: http://poj.org/problem?id=2774
	 * 
	 * 题目描述:
	 * 求两个字符串的最长公共子串长度。
	 * 
	 * 算法思路:
	 * 使用二分答案+滚动哈希
	 * 对可能的长度进行二分，检查是否存在长度为mid的公共子串
	 * 
	 * 时间复杂度: O((n+m)log(min(n,m)))
	 * 空间复杂度: O(n+m)
	 */
	public static int longestCommonSubstring(String s1, String s2) {
		int n = s1.length(), m = s2.length();
		int left = 0, right = Math.min(n, m);
		int result = 0;
		
		long base = 131;
		long mod = (long)1e9 + 7;
		
		while (left <= right) {
			int mid = left + (right - left) / 2;
			
			if (mid == 0) {
				left = mid + 1;
				continue;
			}
			
			Set<Long> set = new HashSet<>();
			long hash = 0;
			long power = 1;
			
			// 计算s1中所有长度为mid的子串哈希值
			for (int i = 0; i < mid; i++) {
				power = (power * base) % mod;
			}
			
			for (int i = 0; i < n; i++) {
				hash = (hash * base + (s1.charAt(i) - 'a' + 1)) % mod;
				if (i >= mid) {
					hash = (hash - (s1.charAt(i - mid) - 'a' + 1) * power % mod + mod) % mod;
				}
				if (i >= mid - 1) {
					set.add(hash);
				}
			}
			
			// 检查s2中是否存在相同的哈希值
			hash = 0;
			boolean found = false;
			
			for (int i = 0; i < m; i++) {
				hash = (hash * base + (s2.charAt(i) - 'a' + 1)) % mod;
				if (i >= mid) {
					hash = (hash - (s2.charAt(i - mid) - 'a' + 1) * power % mod + mod) % mod;
				}
				if (i >= mid - 1 && set.contains(hash)) {
					found = true;
					break;
				}
			}
			
			if (found) {
				result = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return result;
	}

	/**
	 * SPOJ - SUBST1 (不同子串数量)
	 * 题目来源: https://www.spoj.com/problems/SUBST1/
	 * 
	 * 题目描述:
	 * 给定一个字符串，计算其不同子串的数量。
	 * 
	 * 算法思路:
	 * 使用滚动哈希计算所有子串的哈希值，存储在集合中
	 * 集合的大小即为不同子串的数量
	 * 
	 * 时间复杂度: O(n^2)
	 * 空间复杂度: O(n^2)
	 */
	public static int countDistinctSubstrings(String s) {
		int n = s.length();
		Set<Long> set = new HashSet<>();
		long base = 131;
		long mod = (long)1e9 + 7;
		
		for (int i = 0; i < n; i++) {
			long hash = 0;
			for (int j = i; j < n; j++) {
				hash = (hash * base + (s.charAt(j) - 'a' + 1)) % mod;
				set.add(hash);
			}
		}
		
		return set.size();
	}

	/**
	 * AtCoder ABC 284 E - Count Simple Paths (简单路径计数)
	 * 题目来源: https://atcoder.jp/contests/abc284/tasks/abc284_e
	 * 
	 * 题目描述:
	 * 给定无向图，计算从节点1开始的不同简单路径数量。
	 * 简单路径指路径中不包含重复节点。
	 * 
	 * 算法思路:
	 * 使用DFS遍历所有路径，使用哈希集合记录访问过的节点
	 * 使用滚动哈希记录路径的哈希值，避免重复计数
	 * 
	 * 时间复杂度: O(2^n)
	 * 空间复杂度: O(n)
	 */
	public static int countSimplePaths(int n, int[][] edges) {
		List<Integer>[] graph = new ArrayList[n + 1];
		for (int i = 1; i <= n; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for (int[] edge : edges) {
			int u = edge[0], v = edge[1];
			graph[u].add(v);
			graph[v].add(u);
		}
		
		Set<Long> pathHashes = new HashSet<>();
		long base = 131;
		long mod = (long)1e9 + 7;
		
		dfs(1, graph, new boolean[n + 1], 0L, pathHashes, base, mod);
		
		return pathHashes.size();
	}
	
	private static void dfs(int node, List<Integer>[] graph, boolean[] visited, 
						 long currentHash, Set<Long> pathHashes, long base, long mod) {
		visited[node] = true;
		currentHash = (currentHash * base + node) % mod;
		pathHashes.add(currentHash);
		
		for (int neighbor : graph[node]) {
			if (!visited[neighbor]) {
				dfs(neighbor, graph, visited, currentHash, pathHashes, base, mod);
			}
		}
		
		visited[node] = false;
	}

	/**
	 * USACO 2019 December Contest, Gold - Milk Visits (牛奶访问)
	 * 题目来源: http://www.usaco.org/index.php?page=viewproblem2&cpid=970
	 * 
	 * 题目描述:
	 * 给定一棵树，每个节点有一种牛奶类型。
	 * 多个查询，每个查询问从u到v的路径上是否包含特定类型的牛奶。
	 * 
	 * 算法思路:
	 * 使用LCA和路径哈希，对每种牛奶类型建立哈希值
	 * 查询时检查路径哈希值是否包含目标牛奶类型的哈希
	 * 
	 * 时间复杂度: O(n + q log n)
	 * 空间复杂度: O(n)
	 */
	public static boolean[] milkVisits(int n, int[] milkTypes, int[][] edges, int[][] queries) {
		// 构建树
		List<Integer>[] tree = new ArrayList[n + 1];
		for (int i = 1; i <= n; i++) {
			tree[i] = new ArrayList<>();
		}
		
		for (int[] edge : edges) {
			int u = edge[0], v = edge[1];
			tree[u].add(v);
			tree[v].add(u);
		}
		
		// LCA预处理
		int LOG = 20;
		int[][] parent = new int[n + 1][LOG];
		int[] depth = new int[n + 1];
		long[][] pathHash = new long[n + 1][LOG];
		long base = 131;
		long mod = (long)1e9 + 7;
		
		// BFS预处理
		Queue<Integer> queue = new LinkedList<>();
		queue.offer(1);
		depth[1] = 0;
		
		while (!queue.isEmpty()) {
			int u = queue.poll();
			for (int v : tree[u]) {
				if (v == parent[u][0]) continue;
				
				depth[v] = depth[u] + 1;
				parent[v][0] = u;
				pathHash[v][0] = milkTypes[v - 1];
				
				for (int i = 1; i < LOG; i++) {
					parent[v][i] = parent[parent[v][i - 1]][i - 1];
					pathHash[v][i] = (pathHash[v][i - 1] * base + 
								   pathHash[parent[v][i - 1]][i - 1]) % mod;
				}
				
				queue.offer(v);
			}
		}
		
		boolean[] results = new boolean[queries.length];
		
		for (int i = 0; i < queries.length; i++) {
			int u = queries[i][0], v = queries[i][1], targetMilk = queries[i][2];
			
			// 计算u到v路径的哈希值
			long hash = 0;
			
			if (depth[u] < depth[v]) {
				int temp = u;
				u = v;
				v = temp;
			}
			
			// 提升u到v的深度
			for (int j = LOG - 1; j >= 0; j--) {
				if (depth[u] - (1 << j) >= depth[v]) {
					hash = (hash * base + pathHash[u][j]) % mod;
					u = parent[u][j];
				}
			}
			
			if (u == v) {
				hash = (hash * base + milkTypes[u - 1]) % mod;
			} else {
				for (int j = LOG - 1; j >= 0; j--) {
					if (parent[u][j] != parent[v][j]) {
						hash = (hash * base + pathHash[u][j]) % mod;
						hash = (hash * base + pathHash[v][j]) % mod;
						u = parent[u][j];
						v = parent[v][j];
					}
				}
				hash = (hash * base + milkTypes[u - 1]) % mod;
				hash = (hash * base + milkTypes[v - 1]) % mod;
				hash = (hash * base + milkTypes[parent[u][0] - 1]) % mod;
			}
			
			// 检查哈希值是否包含目标牛奶
			long targetHash = targetMilk;
			String pathHashStr = Long.toString(hash);
			String targetHashStr = Long.toString(targetHash);
			
			results[i] = pathHashStr.contains(targetHashStr);
		}
		
		return results;
	}

	/**
	 * 完美哈希 (Perfect Hashing)
	 * 应用场景: 静态数据集，需要O(1)查找时间且无冲突
	 * 算法原理: 使用两级哈希表，第一级哈希将元素分组，第二级为每个组创建无冲突的哈希表
	 * 优势: 保证O(1)查找时间，无哈希冲突
	 * 限制: 仅适用于静态数据集，构建过程较复杂
	 */
	static class PerfectHash {
		private int[][] secondLevelTables;
		private int[] firstLevelTable;
		private String[] keys;
		
		public PerfectHash(String[] keys) {
			this.keys = keys;
			buildPerfectHash();
		}
		
		private void buildPerfectHash() {
			int n = keys.length;
			firstLevelTable = new int[n];
			secondLevelTables = new int[n][];
			
			// 第一级哈希：将元素分组
			List<List<String>> groups = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				groups.add(new ArrayList<>());
			}
			
			for (String key : keys) {
				int hash = Math.abs(key.hashCode()) % n;
				groups.get(hash).add(key);
			}
			
			// 第二级哈希：为每个组创建无冲突哈希表
			for (int i = 0; i < n; i++) {
				List<String> group = groups.get(i);
				if (group.isEmpty()) {
					secondLevelTables[i] = new int[0];
					continue;
				}
				
				int size = group.size() * group.size(); // 平方大小以减少冲突
				int[] table = new int[size];
				Arrays.fill(table, -1);
				
				boolean success = false;
				while (!success) {
					success = true;
					Arrays.fill(table, -1);
					
					for (String key : group) {
						int hash = Math.abs(key.hashCode()) % size;
						if (table[hash] != -1) {
							success = false;
							break;
						}
						table[hash] = Arrays.asList(keys).indexOf(key);
					}
					
					if (!success) {
						size *= 2; // 如果冲突，增加表大小
						table = new int[size];
					}
				}
				
				secondLevelTables[i] = table;
			}
		}
		
		public int get(String key) {
			int firstHash = Math.abs(key.hashCode()) % keys.length;
			int[] secondTable = secondLevelTables[firstHash];
			
			if (secondTable.length == 0) {
				return -1;
			}
			
			int secondHash = Math.abs(key.hashCode()) % secondTable.length;
			int index = secondTable[secondHash];
			
			if (index != -1 && keys[index].equals(key)) {
				return index;
			}
			
			return -1;
		}
	}

	/**
	 * 计数布隆过滤器 (Counting Bloom Filter)
	 * 应用场景: 需要支持删除操作的布隆过滤器变种
	 * 算法原理: 使用计数器数组代替位数组，支持元素的添加和删除
	 * 优势: 支持删除操作，保持布隆过滤器的空间效率
	 * 限制: 空间使用略高于标准布隆过滤器
	 */
	static class CountingBloomFilter {
		private int[] counters;
		private int numHashFunctions;
		private int size;
		
		public CountingBloomFilter(int size, int numHashFunctions) {
			this.size = size;
			this.numHashFunctions = numHashFunctions;
			this.counters = new int[size];
		}
		
		private int[] getHashes(String element) {
			int[] hashes = new int[numHashFunctions];
			int hash1 = Math.abs(element.hashCode());
			int hash2 = hash1 * 31 + 17;
			
			for (int i = 0; i < numHashFunctions; i++) {
				hashes[i] = Math.abs(hash1 + i * hash2) % size;
			}
			
			return hashes;
		}
		
		public void add(String element) {
			int[] hashes = getHashes(element);
			for (int hash : hashes) {
				counters[hash]++;
			}
		}
		
		public void remove(String element) {
			int[] hashes = getHashes(element);
			for (int hash : hashes) {
				if (counters[hash] > 0) {
					counters[hash]--;
				}
			}
		}
		
		public int count(String element) {
			int[] hashes = getHashes(element);
			int minCount = Integer.MAX_VALUE;
			
			for (int hash : hashes) {
				minCount = Math.min(minCount, counters[hash]);
			}
			
			return minCount;
		}
		
		public boolean mightContain(String element) {
			return count(element) > 0;
		}
	}

	/**
	 * 可扩展哈希 (Extendible Hashing)
	 * 应用场景: 数据库索引、文件系统等需要动态扩展的哈希结构
	 * 算法原理: 使用目录结构指向桶，当桶满时进行分裂，目录深度动态调整
	 * 优势: 支持动态扩展，保持较好的性能
	 * 限制: 目录结构增加了一定的空间开销
	 */
	static class ExtendibleHash {
		static class Bucket {
			List<Entry> entries;
			int localDepth;
			
			Bucket(int localDepth) {
				this.localDepth = localDepth;
				this.entries = new ArrayList<>();
			}
		}
		
		static class Entry {
			String key;
			String value;
			
			Entry(String key, String value) {
				this.key = key;
				this.value = value;
			}
		}
		
		private Bucket[] directory;
		private int globalDepth;
		private int bucketSize;
		
		public ExtendibleHash(int bucketSize) {
			this.bucketSize = bucketSize;
			this.globalDepth = 1;
			this.directory = new Bucket[2];
			directory[0] = new Bucket(1);
			directory[1] = new Bucket(1);
		}
		
		private int getHash(String key) {
			return Math.abs(key.hashCode()) & ((1 << globalDepth) - 1);
		}
		
		public void put(String key, String value) {
			int hash = getHash(key);
			Bucket bucket = directory[hash];
			
			// 检查是否已存在该键
			for (Entry entry : bucket.entries) {
				if (entry.key.equals(key)) {
					entry.value = value;
					return;
				}
			}
			
			// 如果桶已满，需要分裂
			if (bucket.entries.size() >= bucketSize) {
				splitBucket(hash);
				put(key, value); // 重新尝试插入
			} else {
				bucket.entries.add(new Entry(key, value));
			}
		}
		
		private void splitBucket(int hash) {
			Bucket bucket = directory[hash];
			
			if (bucket.localDepth == globalDepth) {
				// 需要扩展目录
				extendDirectory();
			}
			
			// 创建新桶
			Bucket newBucket = new Bucket(bucket.localDepth + 1);
			
			// 重新分配条目
			List<Entry> oldEntries = new ArrayList<>(bucket.entries);
			bucket.entries.clear();
			
			int newLocalDepth = bucket.localDepth + 1;
			int mask = (1 << newLocalDepth) - 1;
			
			for (Entry entry : oldEntries) {
				int newHash = Math.abs(entry.key.hashCode()) & mask;
				if ((newHash & (1 << bucket.localDepth)) != 0) {
					newBucket.entries.add(entry);
				} else {
					bucket.entries.add(entry);
				}
			}
			
			// 更新目录指针
			int step = 1 << bucket.localDepth;
			for (int i = hash; i < directory.length; i += step) {
				if ((i & (1 << bucket.localDepth)) != 0) {
					directory[i] = newBucket;
				}
			}
			
			bucket.localDepth++;
		}
		
		private void extendDirectory() {
			int oldSize = directory.length;
			Bucket[] newDirectory = new Bucket[oldSize * 2];
			
			for (int i = 0; i < oldSize; i++) {
				newDirectory[i] = directory[i];
				newDirectory[i + oldSize] = directory[i];
			}
			
			directory = newDirectory;
			globalDepth++;
		}
		
		public String get(String key) {
			int hash = getHash(key);
			Bucket bucket = directory[hash];
			
			for (Entry entry : bucket.entries) {
				if (entry.key.equals(key)) {
					return entry.value;
				}
			}
			
			return null;
		}
		
		public int getDepth() {
			return globalDepth;
		}
	}

	/**
	 * 线性哈希 (Linear Hashing)
	 * 应用场景: 数据库系统、文件系统等需要渐进式扩展的哈希结构
	 * 算法原理: 使用线性探测和分裂策略，避免目录结构的空间开销
	 * 优势: 渐进式扩展，无目录结构开销
	 * 限制: 分裂过程可能影响性能
	 */
	static class LinearHash {
		static class Entry {
			String key;
			String value;
			boolean deleted;
			
			Entry(String key, String value) {
				this.key = key;
				this.value = value;
				this.deleted = false;
			}
		}
		
		private List<Entry>[] table;
		private int size;
		private int splitPointer;
		private int threshold;
		private double loadFactor;
		
		public LinearHash(int initialSize) {
			this.table = new ArrayList[initialSize];
			for (int i = 0; i < initialSize; i++) {
				table[i] = new ArrayList<>();
			}
			this.size = 0;
			this.splitPointer = 0;
			this.threshold = initialSize;
			this.loadFactor = 0.75;
		}
		
		private int hash1(String key) {
			return Math.abs(key.hashCode()) % table.length;
		}
		
		private int hash2(String key) {
			return Math.abs(key.hashCode()) % (table.length * 2);
		}
		
		public void put(String key, String value) {
			int hash = hash1(key);
			
			// 检查是否已存在
			for (Entry entry : table[hash]) {
				if (entry.key.equals(key) && !entry.deleted) {
					entry.value = value;
					return;
				}
			}
			
			// 插入新条目
			table[hash].add(new Entry(key, value));
			size++;
			
			// 检查是否需要分裂
			if (size > threshold * loadFactor) {
				split();
			}
		}
		
		private void split() {
			if (splitPointer >= table.length) {
				return;
			}
			
			// 创建新桶
			List<Entry>[] newTable = new ArrayList[table.length + 1];
			for (int i = 0; i < table.length; i++) {
				newTable[i] = table[i];
			}
			newTable[table.length] = new ArrayList<>();
			
			// 重新哈希splitPointer指向的桶
			List<Entry> oldBucket = table[splitPointer];
			List<Entry> newBucket = new ArrayList<>();
			
			for (Entry entry : oldBucket) {
				if (!entry.deleted) {
					int newHash = hash2(entry.key);
					if (newHash == table.length) {
						newBucket.add(entry);
					}
				}
			}
			
			// 更新桶
			oldBucket.clear();
			for (Entry entry : newBucket) {
				oldBucket.add(entry);
			}
			
			table = newTable;
			splitPointer++;
			
			if (splitPointer >= table.length) {
				splitPointer = 0;
				threshold = table.length;
			}
		}
		
		public String get(String key) {
			int hash = hash1(key);
			
			for (Entry entry : table[hash]) {
				if (entry.key.equals(key) && !entry.deleted) {
					return entry.value;
				}
			}
			
			return null;
		}
		
		public int size() {
			return size;
		}
	}

	public static void main(String[] args) {
		System.out.println("支持的哈希算法 : ");
		Hash.showAlgorithms();
		System.out.println();

		String algorithm = "MD5";
		Hash hash = new Hash(algorithm);
		String str1 = "zuochengyunzuochengyunzuochengyun1";
		String str2 = "zuochengyunzuochengyunzuochengyun2";
		String str3 = "zuochengyunzuochengyunzuochengyun3";
		String str4 = "zuochengyunzuochengyunZuochengyun1";
		String str5 = "zuochengyunzuoChengyunzuochengyun2";
		String str6 = "zuochengyunzuochengyunzuochengyUn3";
		String str7 = "zuochengyunzuochengyunzuochengyun1";
		System.out.println("7个字符串得到的哈希值 : ");
		System.out.println(hash.hashValue(str1));
		System.out.println(hash.hashValue(str2));
		System.out.println(hash.hashValue(str3));
		System.out.println(hash.hashValue(str4));
		System.out.println(hash.hashValue(str5));
		System.out.println(hash.hashValue(str6));
		System.out.println(hash.hashValue(str7));
		System.out.println();

		// 测试LintCode 128题
		System.out.println("=== LintCode 128. Hash Function ===");
		String key = "abcd";
		int HASH_SIZE = 100;
		int result = hashCode(key, HASH_SIZE);
		System.out.println("Key: " + key + ", HASH_SIZE: " + HASH_SIZE + ", Result: " + result);
		System.out.println();

		char[] arr = { 'a', 'b' };
		int n = 20;
		System.out.println("生成长度为n，字符来自arr，所有可能的字符串");
		List<String> strs = generateStrings(arr, n);
//		for (String str : strs) {
//			System.out.println(str);
//		}
		System.out.println("不同字符串的数量 : " + strs.size());
		HashSet<String> set = new HashSet<>();
		for (String str : strs) {
			set.add(hash.hashValue(str));
		}
//		for (String str : set) {
//			System.out.println(str);
//		}
		System.out.println("不同哈希值的数量 : " + set.size());
		System.out.println();

		int m = 13;
		int[] cnts = new int[m];
		System.out.println("现在看看这些哈希值，% " + m + " 之后的余数分布情况");
		BigInteger mod = new BigInteger(String.valueOf(m));
		for (String hashCode : set) {
			BigInteger bigInt = new BigInteger(hashCode, 16);
			int ans = bigInt.mod(mod).intValue();
			cnts[ans]++;
		}
		for (int i = 0; i < m; i++) {
			System.out.println("余数 " + i + " 出现了 " + cnts[i] + " 次");
		}
		
		// 测试LeetCode 705. Design HashSet
		System.out.println("\n=== LeetCode 705. Design HashSet ===");
		MyHashSet myHashSet = new MyHashSet();
		myHashSet.add(1);      // set = [1]
		myHashSet.add(2);      // set = [1, 2]
		System.out.println(myHashSet.contains(1)); // 返回 True
		System.out.println(myHashSet.contains(3)); // 返回 False （未找到）
		myHashSet.add(2);      // set = [1, 2]
		System.out.println(myHashSet.contains(2)); // 返回 True
		myHashSet.remove(2);   // set = [1]
		System.out.println(myHashSet.contains(2)); // 返回 False （已移除）
		
		// 测试LeetCode 706. Design HashMap
		System.out.println("\n=== LeetCode 706. Design HashMap ===");
		MyHashMap myHashMap = new MyHashMap();
		myHashMap.put(1, 1); // myHashMap 现在为 [[1,1]]
		myHashMap.put(2, 2); // myHashMap 现在为 [[1,1], [2,2]]
		System.out.println(myHashMap.get(1));    // 返回 1 ，myHashMap 现在为 [[1,1], [2,2]]
		System.out.println(myHashMap.get(3));    // 返回 -1（未找到），myHashMap 现在为 [[1,1], [2,2]]
		myHashMap.put(2, 1); // myHashMap 现在为 [[1,1], [2,1]]（更新已有的值）
		System.out.println(myHashMap.get(2));    // 返回 1 ，myHashMap 现在为 [[1,1], [2,1]]
		myHashMap.remove(2); // 删除键为 2 的数据，myHashMap 现在为 [[1,1]]
		System.out.println(myHashMap.get(2));    // 返回 -1（未找到），myHashMap 现在为 [[1,1]]
		
		// 测试LeetCode 28. Find the Index of the First Occurrence in a String
		System.out.println("\n=== LeetCode 28. Find the Index of the First Occurrence in a String ===");
		System.out.println(strStr("sadbutsad", "sad")); // 返回 0
		System.out.println(strStr("leetcode", "leeto")); // 返回 -1
		
		// 测试LeetCode 187. Repeated DNA Sequences
		System.out.println("\n=== LeetCode 187. Repeated DNA Sequences ===");
		List<String> dnaResult = findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT");
		System.out.println(dnaResult); // ["AAAAACCCCC","CCCCCAAAAA"]
		
		// 测试LeetCode 214. Shortest Palindrome
		System.out.println("\n=== LeetCode 214. Shortest Palindrome ===");
		System.out.println(shortestPalindrome("aacecaaa")); // "aaacecaaa"
		System.out.println(shortestPalindrome("abcd")); // "dcbabcd"
		
		// 测试一致性哈希
		System.out.println("\n=== 一致性哈希 (Consistent Hashing) ===");
		ConsistentHash consistentHash = new ConsistentHash(100); // 每个服务器100个虚拟节点
		consistentHash.addServer("Server1");
		consistentHash.addServer("Server2");
		consistentHash.addServer("Server3");
		
		System.out.println("键 'user1' 分配到的服务器: " + consistentHash.getServer("user1"));
		System.out.println("键 'user2' 分配到的服务器: " + consistentHash.getServer("user2"));
		System.out.println("键 'user3' 分配到的服务器: " + consistentHash.getServer("user3"));
		
		// 移除一个服务器后，观察键的重新分配情况
		System.out.println("\n移除 Server2 后:");
		consistentHash.removeServer("Server2");
		System.out.println("键 'user1' 分配到的服务器: " + consistentHash.getServer("user1"));
		System.out.println("键 'user2' 分配到的服务器: " + consistentHash.getServer("user2"));
		System.out.println("键 'user3' 分配到的服务器: " + consistentHash.getServer("user3"));
		
		// 测试布隆过滤器
		System.out.println("\n=== 布隆过滤器 (Bloom Filter) ===");
		BloomFilter bloomFilter = new BloomFilter(10000, 7); // 10000位，7个哈希函数
		bloomFilter.add("apple");
		bloomFilter.add("banana");
		bloomFilter.add("orange");
		
		System.out.println("'apple' 可能在集合中: " + bloomFilter.mightContain("apple")); // true
		System.out.println("'banana' 可能在集合中: " + bloomFilter.mightContain("banana")); // true
		System.out.println("'orange' 可能在集合中: " + bloomFilter.mightContain("orange")); // true
		System.out.println("'pear' 可能在集合中: " + bloomFilter.mightContain("pear")); // false
		System.out.println("'grape' 可能在集合中: " + bloomFilter.mightContain("grape")); // false
		
		// 测试双重哈希表
		System.out.println("\n=== 双重哈希 (Double Hashing) ===");
		DoubleHashTable<String, Integer> doubleHashTable = new DoubleHashTable<>();
		doubleHashTable.put("apple", 100);
		doubleHashTable.put("banana", 200);
		doubleHashTable.put("orange", 300);
		
		System.out.println("'apple' 的值: " + doubleHashTable.get("apple")); // 100
		System.out.println("'banana' 的值: " + doubleHashTable.get("banana")); // 200
		System.out.println("'orange' 的值: " + doubleHashTable.get("orange")); // 300
		System.out.println("'pear' 的值: " + doubleHashTable.get("pear")); // null
		
		doubleHashTable.remove("banana");
		System.out.println("移除 'banana' 后的值: " + doubleHashTable.get("banana")); // null
		System.out.println("哈希表大小: " + doubleHashTable.size()); // 2
		
		// 测试更多哈希相关题目
		System.out.println("
=== 更多哈希相关题目测试 ===");
		
		// 测试LeetCode 1. Two Sum
		System.out.println("
=== LeetCode 1. Two Sum ===");
		int[] nums = {2, 7, 11, 15};
		int target = 9;
		int[] twoSumResult = twoSum(nums, target);
		System.out.println("nums: " + Arrays.toString(nums) + ", target: " + target + ", result: " + Arrays.toString(twoSumResult));
		
		// 测试LeetCode 49. Group Anagrams
		System.out.println("
=== LeetCode 49. Group Anagrams ===");
		String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
		List<List<String>> anagramResult = groupAnagrams(strs);
		System.out.println("strs: " + Arrays.toString(strs));
		System.out.println("grouped anagrams: " + anagramResult);
		
		// 测试LeetCode 242. Valid Anagram
		System.out.println("
=== LeetCode 242. Valid Anagram ===");
		String s1 = "anagram", s2 = "nagaram";
		boolean anagramCheck = isAnagram(s1, s2);
		System.out.println("'" + s1 + "' and '" + s2 + "' are anagrams: " + anagramCheck);
		
		// 测试LeetCode 3. Longest Substring Without Repeating Characters
		System.out.println("
=== LeetCode 3. Longest Substring Without Repeating Characters ===");
		String s = "abcabcbb";
		int longestSubstring = lengthOfLongestSubstring(s);
		System.out.println("String: '" + s + "', longest substring length: " + longestSubstring);
		
		// 测试LeetCode 76. Minimum Window Substring
		System.out.println("
=== LeetCode 76. Minimum Window Substring ===");
		String sStr = "ADOBECODEBANC";
		String tStr = "ABC";
		String minWindowResult = minWindow(sStr, tStr);
		System.out.println("s: '" + sStr + "', t: '" + tStr + "', min window: '" + minWindowResult + "'");
		
		// 测试LeetCode 560. Subarray Sum Equals K
		System.out.println("
=== LeetCode 560. Subarray Sum Equals K ===");
		int[] sumNums = {1, 1, 1};
		int k = 2;
		int subarraySumResult = subarraySum(sumNums, k);
		System.out.println("nums: " + Arrays.toString(sumNums) + ", k: " + k + ", subarray count: " + subarraySumResult);
		
		// 测试LeetCode 347. Top K Frequent Elements
		System.out.println("
=== LeetCode 347. Top K Frequent Elements ===");
		int[] freqNums = {1, 1, 1, 2, 2, 3};
		int kFreq = 2;
		int[] topKFrequentResult = topKFrequent(freqNums, kFreq);
		System.out.println("nums: " + Arrays.toString(freqNums) + ", k: " + kFreq + ", top k frequent: " + Arrays.toString(topKFrequentResult));
		
		// 测试LeetCode 380. Insert Delete GetRandom O(1)
		System.out.println("
=== LeetCode 380. Insert Delete GetRandom O(1) ===");
		RandomizedSet randomizedSet = new RandomizedSet();
		System.out.println("Insert 1: " + randomizedSet.insert(1));
		System.out.println("Insert 2: " + randomizedSet.insert(2));
		System.out.println("Insert 1 again: " + randomizedSet.insert(1));
		System.out.println("Get random: " + randomizedSet.getRandom());
		System.out.println("Remove 2: " + randomizedSet.remove(2));
		System.out.println("Remove 3: " + randomizedSet.remove(3));
		System.out.println("Get random: " + randomizedSet.getRandom());
		
		// 测试LeetCode 146. LRU Cache
		System.out.println("
=== LeetCode 146. LRU Cache ===");
		LRUCache lruCache = new LRUCache(2);
		lruCache.put(1, 1);
		lruCache.put(2, 2);
		System.out.println("Get 1: " + lruCache.get(1));
		lruCache.put(3, 3);  // 这会使得键2被移除
		System.out.println("Get 2: " + lruCache.get(2));
		System.out.println("Get 3: " + lruCache.get(3));
		
		// 测试Codeforces 271D - Good Substrings
		System.out.println("
=== Codeforces 271D - Good Substrings ===");
		String sGood = "abacaba";
		String bad = "00000000000000000000000000"; // 所有字母都是好的
		int kGood = 1;
		int goodSubstrings = countGoodSubstrings(sGood, bad, kGood);
		System.out.println("String: " + sGood + ", bad: " + bad + ", k: " + kGood + ", good substrings: " + goodSubstrings);
		
		// 测试Codeforces 514C - Watto and Mechanism
		System.out.println("
=== Codeforces 514C - Watto and Mechanism ===");
		String[] dictionary = {"abc", "def", "ghi"};
		String[] queries = {"abc", "dbc", "efg"};
		boolean[] mechanismResults = wattoAndMechanism(dictionary, queries);
		System.out.println("Dictionary: " + Arrays.toString(dictionary));
		System.out.println("Queries: " + Arrays.toString(queries));
		System.out.println("Results: " + Arrays.toString(mechanismResults));
		
		// 测试Codeforces 835D - Palindromic characteristics
		System.out.println("
=== Codeforces 835D - Palindromic characteristics ===");
		String sPal = "ababa";
		int[] palResults = palindromicCharacteristics(sPal);
		System.out.println("String: " + sPal);
		System.out.println("Palindromic characteristics: " + Arrays.toString(palResults));
		
		// 测试剑指Offer题目
		System.out.println("
=== 剑指Offer 50. 第一个只出现一次的字符 ===");
		String sOffer = "abaccdeff";
		char firstUniq = firstUniqChar(sOffer);
		System.out.println("String: " + sOffer + ", first unique char: " + firstUniq);
		
		System.out.println("
=== 剑指Offer 03. 数组中重复的数字 ===");
		int[] numsOffer = {2, 3, 1, 0, 2, 5, 3};
		int repeatNum = findRepeatNumber(numsOffer);
		System.out.println("Nums: " + Arrays.toString(numsOffer) + ", repeat number: " + repeatNum);
		
		System.out.println("
=== 剑指Offer 48. 最长不含重复字符的子字符串 ===");
		String sSubstring = "abcabcbb";
		int longestSubstring2 = lengthOfLongestSubstring2(sSubstring);
		System.out.println("String: " + sSubstring + ", longest substring length: " + longestSubstring2);
		
		// 测试HDU 4821 - String
		System.out.println("
=== HDU 4821 - String ===");
		String sHDU = "abcabcabc";
		int M = 3, L = 3;
		int validSubstrings = countValidSubstrings(sHDU, M, L);
		System.out.println("String: " + sHDU + ", M: " + M + ", L: " + L + ", valid substrings: " + validSubstrings);
		
		// 测试POJ 2774 - Long Long Message
		System.out.println("
=== POJ 2774 - Long Long Message ===");
		String s1POJ = "abcdefg";
		String s2POJ = "cdefghij";
		int lcsLength = longestCommonSubstring(s1POJ, s2POJ);
		System.out.println("String1: " + s1POJ + ", String2: " + s2POJ + ", LCS length: " + lcsLength);
		
		// 测试SPOJ - SUBST1
		System.out.println("
=== SPOJ - SUBST1 ===");
		String sSPOJ = "abc";
		int distinctSubstrings = countDistinctSubstrings(sSPOJ);
		System.out.println("String: " + sSPOJ + ", distinct substrings: " + distinctSubstrings);
		
		// 测试AtCoder ABC 284 E - Count Simple Paths
		System.out.println("
=== AtCoder ABC 284 E - Count Simple Paths ===");
		int nAtCoder = 3;
		int[][] edgesAtCoder = {{1, 2}, {2, 3}};
		int simplePaths = countSimplePaths(nAtCoder, edgesAtCoder);
		System.out.println("Nodes: " + nAtCoder + ", edges: " + Arrays.deepToString(edgesAtCoder) + ", simple paths: " + simplePaths);
		
		// 测试USACO 2019 December Contest, Gold - Milk Visits
		System.out.println("
=== USACO 2019 December Contest, Gold - Milk Visits ===");
		int nUSACO = 4;
		int[] milkTypes = {1, 2, 1, 2};
		int[][] edgesUSACO = {{1, 2}, {2, 3}, {2, 4}};
		int[][] queriesUSACO = {{1, 3, 1}, {1, 4, 2}};
		boolean[] milkResults = milkVisits(nUSACO, milkTypes, edgesUSACO, queriesUSACO);
		System.out.println("Nodes: " + nUSACO + ", milk types: " + Arrays.toString(milkTypes));
		System.out.println("Queries: " + Arrays.deepToString(queriesUSACO) + ", results: " + Arrays.toString(milkResults));
		
		// 测试高级哈希应用
		System.out.println("
=== 高级哈希应用测试 ===");
		
		// 测试完美哈希
		System.out.println("
=== 完美哈希 (Perfect Hashing) ===");
		String[] keys = {"apple", "banana", "orange", "grape", "pear"};
		PerfectHash perfectHash = new PerfectHash(keys);
		for (String key : keys) {
			System.out.println("Key '" + key + "' 的哈希值: " + perfectHash.get(key));
		}
		System.out.println("Key 'mango' 的哈希值: " + perfectHash.get("mango")); // 应该返回-1
		
		// 测试计数布隆过滤器
		System.out.println("
=== 计数布隆过滤器 (Counting Bloom Filter) ===");
		CountingBloomFilter countingBloomFilter = new CountingBloomFilter(10000, 7);
		countingBloomFilter.add("apple");
		countingBloomFilter.add("banana");
		countingBloomFilter.add("apple"); // 重复添加
		System.out.println("'apple' 计数: " + countingBloomFilter.count("apple")); // 应该为2
		System.out.println("'banana' 计数: " + countingBloomFilter.count("banana")); // 应该为1
		System.out.println("'orange' 计数: " + countingBloomFilter.count("orange")); // 应该为0
		
		countingBloomFilter.remove("apple");
		System.out.println("移除一个 'apple' 后计数: " + countingBloomFilter.count("apple")); // 应该为1
		
		// 测试可扩展哈希
		System.out.println("
=== 可扩展哈希 (Extendible Hashing) ===");
		ExtendibleHash extendibleHash = new ExtendibleHash(2); // 每个桶最多2个元素
		extendibleHash.put("key1", "value1");
		extendibleHash.put("key2", "value2");
		extendibleHash.put("key3", "value3"); // 这会触发桶分裂
		System.out.println("key1: " + extendibleHash.get("key1"));
		System.out.println("key2: " + extendibleHash.get("key2"));
		System.out.println("key3: " + extendibleHash.get("key3"));
		System.out.println("目录深度: " + extendibleHash.getDepth());
		
		// 测试线性哈希
		System.out.println("
=== 线性哈希 (Linear Hashing) ===");
		LinearHash linearHash = new LinearHash(4); // 初始大小为4
		linearHash.put("key1", "value1");
		linearHash.put("key2", "value2");
		linearHash.put("key3", "value3");
		linearHash.put("key4", "value4");
		linearHash.put("key5", "value5"); // 这会触发分裂
		System.out.println("key1: " + linearHash.get("key1"));
		System.out.println("key2: " + linearHash.get("key2"));
		System.out.println("key3: " + linearHash.get("key3"));
		System.out.println("key4: " + linearHash.get("key4"));
		System.out.println("key5: " + linearHash.get("key5"));
		System.out.println("哈希表大小: " + linearHash.size());
	}
}
}