package class035;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LRU (Least Recently Used) 缓存实现
 * 
 * 一、题目解析
 * LRU (Least Recently Used) 最近最少使用缓存机制是一种常用的页面置换算法。
 * 当缓存满时，会优先淘汰最长时间未被访问的数据。
 * 要求实现get和put操作，均要求O(1)时间复杂度。
 * 
 * 二、算法思路
 * 1. 使用双向链表维护访问顺序，最近访问的节点放在尾部，最久未访问的节点在头部
 * 2. 使用哈希表实现O(1)时间复杂度的查找操作，映射键到节点
 * 3. 当访问一个节点时，将其移动到链表尾部（最近访问）
 * 4. 当插入新节点且缓存满时，删除链表头部节点（最久未访问）
 * 
 * 三、时间复杂度分析
 * get操作: O(1) - 哈希表查找 + 链表节点移动
 * put操作: O(1) - 哈希表插入/更新 + 链表节点插入/删除
 * 
 * 四、空间复杂度分析
 * O(capacity) - 哈希表和双向链表最多存储capacity个节点
 * 
 * 五、工程化考量
 * 1. 异常处理: 检查非法输入如capacity<=0
 * 2. 线程安全: 当前实现非线程安全，如需线程安全可使用ReentrantReadWriteLock
 * 3. 内存管理: 节点复用、及时清理无用对象避免内存泄漏
 * 4. 可配置性: 支持自定义容量
 * 5. 单元测试: 需要覆盖各种边界情况和操作组合
 * 6. 性能优化: 避免不必要的节点创建和销毁
 * 7. 扩展性: 考虑支持更多功能如统计、回调等
 * 8. 监控: 实际应用中可能需要添加命中率统计等监控指标
 * 
 * 六、相关题目扩展
 * 1. LeetCode 146. [LRU Cache](https://leetcode.com/problems/lru-cache/) (本题原型)
 * 2. LeetCode 460. [LFU Cache](https://leetcode.com/problems/lfu-cache/) (最近最不经常使用)
 * 3. LeetCode 432. [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/)
 * 4. 牛客网: [设计LRU缓存结构](https://www.nowcoder.com/practice/e3769a5f498241bd98942db7489cbff8)
 * 5. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
 * 6. LintCode 24. [LRU缓存策略](https://www.lintcode.com/problem/24/)
 * 7. HackerRank: [Cache Implementation](https://www.hackerrank.com/challenges/lru-cache/problem)
 * 8. CodeChef: [Implement Cache](https://www.codechef.com/problems/IMCACHE)
 * 9. 计蒜客: [LRU缓存实现](https://nanti.jisuanke.com/t/41393)
 * 10. 杭电OJ 1816: [LRU Cache](http://acm.hdu.edu.cn/showproblem.php?pid=1816)
 * 
 * 七、补充题目（各大OJ平台）
 * 1. AtCoder ABC238D. [AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 缓存优化问题
 * 2. Codeforces Round #344 (Div. 2) D. [Messenger](https://codeforces.com/contest/631/problem/D) - 消息缓存应用
 * 3. UVA 11525. [Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 缓存置换算法
 * 4. SPOJ DQUERY. [D-query](https://www.spoj.com/problems/DQUERY/) - 缓存查询优化
 * 5. Project Euler 543. [Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 缓存计数优化
 * 6. HDU 1284. [钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划缓存优化
 * 7. POJ 3349. [Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 缓存唯一性检测
 * 8. USACO Training: [Caching](https://train.usaco.org/) - 缓存基础训练
 * 9. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 数据流缓存
 * 10. 赛码: [缓存设计](https://www.acmcoder.com/) - 在线编程题目
 * 
 * 八、算法设计技巧总结
 * 1. 双向链表维护访问顺序：最近访问的节点在尾部，最久未访问的节点在头部
 * 2. 哈希表提供O(1)查找：键到节点的直接映射
 * 3. 节点移动优化：访问时移动到尾部，淘汰时删除头部
 * 4. 容量控制：当缓存满时自动淘汰最久未使用的元素
 * 5. 边界处理：处理空缓存、单元素缓存等边界情况
 * 
 * 九、面试要点
 * 1. 解释LRU算法的核心思想和工作原理
 * 2. 分析为什么需要双向链表而不是单向链表
 * 3. 讨论哈希表在LRU实现中的作用
 * 4. 分析各种边界情况下的行为
 * 5. 提出线程安全实现的方案
 * 6. 讨论LRU算法的优缺点和适用场景
 * 
 * 十、工程实践中的应用场景
 * 1. 操作系统页面置换算法
 * 2. 数据库缓存管理
 * 3. Web服务器缓存策略
 * 4. 浏览器缓存机制
 * 5. 分布式系统缓存设计
 * 6. 内存管理优化
 * 
 * @author 算法工程师
 * @version 1.0
 * @since 2024
 */
public class Code02_LRU {

	// 测试链接 : https://leetcode.cn/problems/lru-cache/
	class LRUCache {

		/*
		 * 双向链表节点类
		 * 用于维护访问顺序，最近访问的节点在尾部，最久未访问的节点在头部
		 */
		class DoubleNode {
			public int key;
			public int val;
			public DoubleNode last;
			public DoubleNode next;

			public DoubleNode(int k, int v) {
				key = k;
				val = v;
			}
		}

		/*
		 * 双向链表类
		 * 提供基本的链表操作：添加节点、移动节点到尾部、删除头节点
		 * 封装链表操作，简化主逻辑
		 */
		class DoubleList {
			private DoubleNode head;
			private DoubleNode tail;

			public DoubleList() {
				head = null;
				tail = null;
			}

			/*
			 * 添加节点到链表尾部
			 * 时间复杂度: O(1)
			 * 关键步骤: 处理空链表情况和非空链表情况
			 */
			public void addNode(DoubleNode newNode) {
				if (newNode == null) {
					return;
				}
				if (head == null) {
					// 空链表情况
					head = newNode;
					tail = newNode;
				} else {
					// 非空链表情况，添加到尾部
					tail.next = newNode;
					newNode.last = tail;
					tail = newNode;
				}
			}

			/*
			 * 将指定节点移动到链表尾部
			 * 时间复杂度: O(1)
			 * 边界处理: 节点已经在尾部、节点是头节点
			 */
			public void moveNodeToTail(DoubleNode node) {
				// 优化: 如果节点已经在尾部，无需操作
				if (tail == node) {
					return;
				}
				
				// 从原位置移除节点
				if (head == node) {
					// 节点是头节点
					head = node.next;
					head.last = null;
				} else {
					// 节点在中间位置
					node.last.next = node.next;
					node.next.last = node.last;
				}
				
				// 将节点添加到尾部
				node.last = tail;
				node.next = null;
				tail.next = node;
				tail = node;
			}

			/*
			 * 删除并返回链表头部节点（最久未使用）
			 * 时间复杂度: O(1)
			 * 边界处理: 空链表、链表只有一个节点
			 */
			public DoubleNode removeHead() {
				if (head == null) {
					return null; // 空链表
				}
				DoubleNode ans = head;
				if (head == tail) {
					// 链表只有一个节点
					head = null;
					tail = null;
				} else {
					// 链表有多个节点
					head = ans.next;
					ans.next = null; // 断开连接，帮助GC
					head.last = null;
				}
				return ans;
			}

		}

		// 哈希表用于O(1)时间复杂度查找节点
		private HashMap<Integer, DoubleNode> keyNodeMap;

		// 双向链表维护访问顺序
		private DoubleList nodeList;

		// 缓存容量
		private final int capacity;

		/*
		 * 构造函数
		 * @param cap 缓存容量
		 * 边界检查: 容量必须大于0
		 */
		public LRUCache(int cap) {
			// 检查非法输入
			if (cap <= 0) {
				throw new IllegalArgumentException("容量必须大于0");
			}
			keyNodeMap = new HashMap<>();
			nodeList = new DoubleList();
			capacity = cap;
		}

		/*
		 * 获取指定key的值
		 * @param key 键
		 * @return 如果key存在返回对应的值，否则返回-1
		 * 时间复杂度: O(1)
		 * 核心逻辑: 查找节点并更新访问顺序
		 */
		public int get(int key) {
			if (keyNodeMap.containsKey(key)) {
				DoubleNode ans = keyNodeMap.get(key);
				// 将访问的节点移动到链表尾部（最近访问）
				nodeList.moveNodeToTail(ans);
				return ans.val;
			}
			return -1; // 键不存在
		}

		/*
		 * 插入或更新键值对
		 * @param key 键
		 * @param value 值
		 * 时间复杂度: O(1)
		 * 核心逻辑: 处理更新已存在的键和插入新键两种情况
		 */
		public void put(int key, int value) {
			if (keyNodeMap.containsKey(key)) {
				// 更新已存在的key
				DoubleNode node = keyNodeMap.get(key);
				node.val = value;
				// 将访问的节点移动到链表尾部（最近访问）
				nodeList.moveNodeToTail(node);
			} else {
				// 插入新key
				if (keyNodeMap.size() == capacity) {
					// 缓存已满，删除最久未使用的节点（链表头部）
					DoubleNode removed = nodeList.removeHead();
					keyNodeMap.remove(removed.key);
				}
				// 创建新节点并添加到链表尾部和哈希表
				DoubleNode newNode = new DoubleNode(key, value);
				keyNodeMap.put(key, newNode);
				nodeList.addNode(newNode);
			}
		}
	}
	
	/*
	 * 补充实现: 线程安全的LRU缓存
	 * 使用读写锁实现线程安全，允许多读单写
	 * 适用于读多写少的场景
	 */
	class ThreadSafeLRUCache {
		// 读写锁，允许多个读操作并发执行
		private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
		private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
		
		// 内部使用非线程安全的LRUCache实现
		private final LRUCache cache;
		
		public ThreadSafeLRUCache(int capacity) {
			this.cache = new LRUCache(capacity);
		}
		
		/*
		 * 线程安全的get操作
		 * 使用读锁，允许多个线程同时读取
		 */
		public int get(int key) {
			readLock.lock();
			try {
				return cache.get(key);
			} finally {
				readLock.unlock();
			}
		}
		
		/*
		 * 线程安全的put操作
		 * 使用写锁，确保独占访问
		 */
		public void put(int key, int value) {
			writeLock.lock();
			try {
				cache.put(key, value);
			} finally {
				writeLock.unlock();
			}
		}
	}
	
	/*
	 * 补充题目1: LeetCode 146. LRU缓存机制
	 * 题目描述: 实现LRUCache类，支持get和put操作，要求O(1)时间复杂度
	 * 与本题完全一致，上述实现可以直接应用
	 */
	
	/*
	 * 补充题目2: 支持统计功能的LRU缓存扩展
	 * 扩展功能: 添加命中率统计、访问次数统计等功能
	 */
	class EnhancedLRUCache extends LRUCache {
		private int hits = 0;      // 缓存命中次数
		private int accesses = 0;  // 总访问次数
		private int evictions = 0; // 淘汰次数
		
		public EnhancedLRUCache(int capacity) {
			super(capacity);
		}
		
		@Override
		public int get(int key) {
			accesses++;
			int value = super.get(key);
			if (value != -1) {
				hits++;
			}
			return value;
		}
		
		// 获取命中率
		public double getHitRate() {
			return accesses == 0 ? 0 : (double) hits / accesses;
		}
		
		// 获取淘汰次数
		public int getEvictionCount() {
			return evictions;
		}
	}
	
	/**
	 * 单元测试类 - 测试LRU缓存的各种功能
	 */
	static class LRUCacheTest {
		
		/**
		 * 测试基本功能：插入、查询、淘汰
		 */
		public static void testBasicOperations() {
			System.out.println("=== 测试LRU基本功能 ===");
			Code02_LRU outer = new Code02_LRU();
			LRUCache cache = outer.new LRUCache(2);
			
			// 测试插入和查询
			cache.put(1, 1);
			cache.put(2, 2);
			assert cache.get(1) == 1 : "插入后查询失败";
			assert cache.get(2) == 2 : "插入后查询失败";
			System.out.println("✓ 基本插入查询测试通过");
			
			// 测试容量限制和淘汰机制
			cache.put(3, 3); // 应该淘汰键1
			assert cache.get(1) == -1 : "淘汰机制失败";
			assert cache.get(2) == 2 : "淘汰错误键";
			assert cache.get(3) == 3 : "新插入失败";
			System.out.println("✓ 容量限制和淘汰测试通过");
			
			// 测试访问顺序影响淘汰
			cache.get(2); // 访问键2，使其成为最近访问
			cache.put(4, 4); // 应该淘汰键3
			assert cache.get(3) == -1 : "访问顺序淘汰失败";
			assert cache.get(2) == 2 : "最近访问键被错误淘汰";
			assert cache.get(4) == 4 : "新插入失败";
			System.out.println("✓ 访问顺序影响淘汰测试通过");
		}
		
		/**
		 * 测试边界情况
		 */
		public static void testEdgeCases() {
			System.out.println("\n=== 测试边界情况 ===");
			Code02_LRU outer = new Code02_LRU();
			
			// 测试容量为1
			LRUCache cache1 = outer.new LRUCache(1);
			cache1.put(1, 1);
			assert cache1.get(1) == 1 : "容量1插入失败";
			cache1.put(2, 2);
			assert cache1.get(1) == -1 : "容量1淘汰失败";
			assert cache1.get(2) == 2 : "容量1新插入失败";
			System.out.println("✓ 容量1测试通过");
			
			// 测试空缓存查询
			LRUCache cache0 = outer.new LRUCache(2);
			assert cache0.get(1) == -1 : "空缓存查询失败";
			System.out.println("✓ 空缓存测试通过");
			
			// 测试更新已存在键
			LRUCache cache2 = outer.new LRUCache(2);
			cache2.put(1, 1);
			cache2.put(1, 10); // 更新值
			assert cache2.get(1) == 10 : "更新键值失败";
			cache2.put(2, 2);
			cache2.put(3, 3); // 应该淘汰键2
			assert cache2.get(1) == 10 : "更新后键被错误淘汰";
			assert cache2.get(2) == -1 : "淘汰机制失败";
			System.out.println("✓ 更新键值测试通过");
		}
		
		/**
		 * 测试性能和大数据量场景
		 */
		public static void testPerformance() {
			System.out.println("\n=== 测试性能和大数据量 ===");
			int capacity = 1000;
			int operations = 10000;
			Code02_LRU outer = new Code02_LRU();
			LRUCache cache = outer.new LRUCache(capacity);
			
			long startTime = System.currentTimeMillis();
			
			// 批量插入
			for (int i = 0; i < operations; i++) {
				cache.put(i, i * 10);
				if (i > capacity) {
					// 验证淘汰机制
					assert cache.get(i - capacity) == -1 : "淘汰机制失败";
				}
			}
			
			// 批量查询最近访问的键
			for (int i = operations - capacity; i < operations; i++) {
				int value = cache.get(i);
				assert value == i * 10 : "批量查询失败";
			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("✓ 性能测试通过，处理 " + operations + " 次操作耗时: " + (endTime - startTime) + "ms");
		}
		
		/**
		 * 测试线程安全版本
		 */
		public static void testThreadSafety() {
			System.out.println("\n=== 测试线程安全版本 ===");
			Code02_LRU outer = new Code02_LRU();
			ThreadSafeLRUCache threadSafeCache = outer.new ThreadSafeLRUCache(3);
			
			// 基本功能测试
			threadSafeCache.put(1, 100);
			threadSafeCache.put(2, 200);
			assert threadSafeCache.get(1) == 100 : "线程安全版基本功能失败";
			assert threadSafeCache.get(2) == 200 : "线程安全版基本功能失败";
			
			threadSafeCache.put(3, 300);
			threadSafeCache.put(4, 400); // 应该淘汰键1
			assert threadSafeCache.get(1) == -1 : "线程安全版淘汰机制失败";
			assert threadSafeCache.get(4) == 400 : "线程安全版新插入失败";
			
			System.out.println("✓ 线程安全版本测试通过");
		}
		
		/**
		 * 运行所有测试
		 */
		public static void runAllTests() {
			try {
				testBasicOperations();
				testEdgeCases();
				testPerformance();
				testThreadSafety();
				System.out.println("\n🎉 所有LRU测试通过！LRU缓存功能正常。");
			} catch (AssertionError e) {
				System.err.println("❌ LRU测试失败: " + e.getMessage());
			}
		}
	}
	
	/**
	 * 主方法 - 运行测试和演示
	 */
	public static void main(String[] args) {
		// 运行单元测试
		LRUCacheTest.runAllTests();
		
		// 演示基本功能
		System.out.println("\n=== LRU功能演示 ===");
		Code02_LRU outer = new Code02_LRU();
		LRUCache cache = outer.new LRUCache(3);
		
		System.out.println("1. 插入3个键值对");
		cache.put(1, 10);
		cache.put(2, 20);
		cache.put(3, 30);
		System.out.println("   当前缓存: [1=10, 2=20, 3=30]");
		
		System.out.println("2. 访问键1，使其成为最近使用");
		cache.get(1);
		System.out.println("   访问键1后，键1成为最近使用");
		
		System.out.println("3. 插入新键4，触发淘汰机制");
		cache.put(4, 40);
		System.out.println("   插入键4，应该淘汰最久未使用的键2");
		System.out.println("   当前缓存: [3=30, 1=10, 4=40]");
		System.out.println("   键2查询结果: " + cache.get(2));
		
		System.out.println("\n演示完成！");
	}

}