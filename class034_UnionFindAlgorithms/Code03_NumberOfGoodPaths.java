package class057;

import java.util.Arrays;

/**
 * 好路径的数目
 * 给你一棵 n 个节点的树（连通无向无环的图）
 * 节点编号从0到n-1，且恰好有n-1条边
 * 给你一个长度为 n 下标从 0 开始的整数数组 vals
 * 分别表示每个节点的值。同时给你一个二维整数数组 edges
 * 其中 edges[i] = [ai, bi] 表示节点 ai 和 bi 之间有一条 无向 边
 * 好路径需要满足以下条件：开始和结束节点的值相同、 路径中所有值都小于等于开始的值
 * 请你返回不同好路径的数目
 * 注意，一条路径和它反向的路径算作 同一 路径
 * 比方说， 0 -> 1 与 1 -> 0 视为同一条路径。单个节点也视为一条合法路径
 * 
 * 测试链接 : https://leetcode.cn/problems/number-of-good-paths/
 * 
 * 算法思路：
 * 1. 使用并查集来管理连通分量
 * 2. 关键创新：让较大值的节点作为集合的代表元素
 * 3. 按照边连接的两个节点的最大值从小到大处理边
 * 4. 当合并两个具有相同最大值的集合时，计算新增的好路径数目
 * 
 * 算法思路深度解析：
 * - 该问题的核心挑战在于确保路径上的所有节点值都不超过端点值
 * - 传统的并查集主要用于连通性问题，但该问题还需要维护值的限制条件
 * - 创新点：
 *   1. 按边连接的两个节点的最大值排序，确保在处理一条边时，两个集合中的所有节点值都不超过该边的最大值
 *   2. 每个集合由其中最大值节点作为代表，这样可以快速确定两个集合合并时是否会产生新的好路径
 *   3. 维护每个集合中最大值出现的次数，当合并两个具有相同最大值的集合时，新增的好路径数等于两个集合中最大值节点数的乘积
 * - 算法正确性保证：
 *   1. 单个节点构成的路径会被初始计数
 *   2. 对于非单节点路径，由于边按最大值排序，确保了在计算路径时所有中间节点值都不超过端点值
 *   3. 路径方向不会重复计算，因为是通过合并集合的方式计数，而不是枚举所有可能的路径
 * 
 * 时间复杂度分析：
 * - 边排序的时间复杂度: O(E log E)，其中E是边的数量
 * - 并查集的find和union操作: O(E α(n))，其中α(n)是阿克曼函数的反函数，在实际应用中可视为常数
 * - 由于树有n个节点和n-1条边，所以总体时间复杂度: O(n log n)
 * 
 * 空间复杂度分析：
 * - 并查集数组: O(n)
 * - 边数组排序需要的额外空间: O(log E)
 * - 总体空间复杂度: O(n)
 * 
 * 是否为最优解：是，该方法通过巧妙的边排序和并查集设计，有效地将问题从可能的O(n^2)复杂度降低到O(n log n)
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查输入参数的有效性，如空数组
 *    - 处理边界情况，如只有一个节点的树
 *    - 验证边的合法性（节点编号在有效范围内）
 * 2. 内存优化：
 *    - 使用静态数组预分配空间，避免频繁的动态内存分配
 *    - MAXN常量可以根据题目约束灵活调整
 * 3. 可扩展性：
 *    - 该算法模式可以应用于类似的带值约束的路径计数问题
 *    - 可以扩展处理图（非树）结构，但需要额外处理环
 * 4. 线程安全：
 *    - 当前实现不是线程安全的
 *    - 在多线程环境中需要添加同步机制或使用线程本地存储
 * 5. 性能优化：
 *    - 使用路径压缩优化并查集的查找操作
 *    - 按边的最大节点值排序，避免不必要的合并操作
 * 
 * 与其他算法的对比：
 * 1. 暴力枚举：
 *    - 对于每个节点对，检查路径是否符合条件，时间复杂度为O(n^3)，显然不可行
 * 2. 深度优先搜索(DFS)或广度优先搜索(BFS)：
 *    - 对于每个节点作为端点，进行受限搜索，时间复杂度为O(n^2)
 *    - 远不如并查集方法高效
 * 3. 并查集优化：
 *    - 本方法的关键优化是按边的最大值排序和维护集合中的最大值信息
 *    - 这使得合并操作能够高效地计算新增的好路径数目
 * 
 * 极端情况分析：
 * 1. 所有节点值相同：好路径数目为n*(n+1)/2
 * 2. 所有节点值互不相同：好路径数目为n（只有单节点路径）
 * 3. 树退化为链表：算法依然高效工作
 * 4. 只有一个节点：返回1（单节点路径）
 * 5. 星型结构树：中心节点连接所有其他节点，算法同样高效
 * 
 * 调试技巧：
 * 1. 打印每个边处理前后的并查集状态
 * 2. 验证maxcnt数组的正确性，确保它确实反映了集合中最大值节点的数量
 * 3. 使用小规模测试用例手动模拟算法执行过程，验证路径计数的正确性
 * 4. 检查排序后的边顺序是否正确
 * 
 * 问题迁移能力：
 * 掌握此问题的解法后，可以解决类似的带值约束的连通性问题：
 * - 计算满足特定值条件的路径数目
 * - 处理有值约束的连通分量问题
 * - 解决基于值的图分割问题
 * 
 * 与高级数据结构的结合：
 * 该问题展示了如何将并查集与其他技术（如排序、贪心策略）结合使用，
 * 这种组合在解决复杂的图论问题时非常有效
 */
public class Code03_NumberOfGoodPaths {

	// 最大节点数量限制，根据题目约束设置
	public static int MAXN = 30001;

	// 并查集父节点数组 - 需要保证集合中，代表节点的值一定是整个集合的最大值
	// 这是该算法的关键设计，确保每个集合的代表元素是该集合中的最大值节点
	public static int[] father = new int[MAXN];

	// 记录每个集合中最大值出现的次数
	// maxcnt[代表元素] = 该集合中最大值的节点数量
	// 用于计算合并两个具有相同最大值的集合时新增的好路径数目
	public static int[] maxcnt = new int[MAXN];

	/**
	 * 初始化并查集
	 * 
	 * @param n 节点数量
	 * @throws IllegalArgumentException 当节点数量无效时抛出异常
	 * @throws ArrayIndexOutOfBoundsException 当节点数量超过MAXN时抛出异常
	 */
	public static void build(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("节点数量必须大于0");
		}
		if (n > MAXN) {
			throw new ArrayIndexOutOfBoundsException("节点数量超过最大限制");
		}
		
		// 每个节点初始时是自己的代表元素
		// 此时每个节点所在的集合只有自己
		for (int i = 0; i < n; i++) {
			father[i] = i;         // 初始时父节点指向自己
			maxcnt[i] = 1;         // 每个节点单独构成一个集合，最大值出现次数为1
		}
	}

	/**
	 * 查找操作，带路径压缩优化
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的代表元素（该集合中的最大值节点）
	 * @throws ArrayIndexOutOfBoundsException 当节点编号超出范围时抛出异常
	 */
	public static int find(int i) {
		if (i < 0 || i >= MAXN) {
			throw new ArrayIndexOutOfBoundsException("节点编号超出范围");
		}
		
		// 路径压缩：将查找路径上的所有节点直接连接到根节点
		// 这大大减少了后续查找操作的时间复杂度，使并查集操作接近O(1)
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

	/**
	 * 合并操作，按照值的大小决定代表元素
	 * 
	 * @param x 第一个节点
	 * @param y 第二个节点
	 * @param vals 节点值数组
	 * @return 合并产生的新好路径数目
	 * @throws NullPointerException 当vals数组为null时抛出异常
	 * @throws IndexOutOfBoundsException 当节点编号超出vals数组范围时抛出异常
	 */
	public static int union(int x, int y, int[] vals) {
		if (vals == null) {
			throw new NullPointerException("节点值数组不能为null");
		}
		if (x < 0 || x >= vals.length || y < 0 || y >= vals.length) {
			throw new IndexOutOfBoundsException("节点编号超出范围");
		}
		
		// fx: x所在集合的代表节点（也是该集合的最大值节点）
		int fx = find(x);
		// fy: y所在集合的代表节点（也是该集合的最大值节点）
		int fy = find(y);
		int newPaths = 0; // 记录此次合并产生的新好路径数目
		
		// 根据两个集合的最大值决定合并方向
		if (vals[fx] > vals[fy]) {
			// x所在集合的最大值较大，将y所在集合合并到x所在集合
			// 代表元素保持为fx，因为它是较大值的节点
			father[fy] = fx;
			// 由于最大值不同，不会产生新的好路径（路径需要两端节点值相同）
		} else if (vals[fx] < vals[fy]) {
			// y所在集合的最大值较大，将x所在集合合并到y所在集合
			// 代表元素保持为fy，因为它是较大值的节点
			father[fx] = fy;
			// 由于最大值不同，不会产生新的好路径
		} else {
			// 两个集合的最大值相同，此时合并会产生新的好路径
			// 新路径数目 = fx集合中最大值节点数 * fy集合中最大值节点数
			// 解释：对于fx集合中的每个最大值节点u和fy集合中的每个最大值节点v，
			// u到v的路径上的所有节点值都不超过vals[fx]，所以这是一条好路径
			newPaths = maxcnt[fx] * maxcnt[fy];
			
			// 将fy集合合并到fx集合
			// 合并方向在这里并不重要，因为两个集合的最大值相同
			father[fy] = fx;
			
			// 更新合并后集合中最大值的出现次数
			// 这是为了后续合并时能正确计算新的好路径数目
			maxcnt[fx] += maxcnt[fy];
		}
		return newPaths;
	}

	/**
	 * 计算好路径的总数
	 * 
	 * @param vals 节点值数组，每个元素代表对应节点的值
	 * @param edges 边数组，每个元素为 [a, b] 表示节点a和节点b之间有一条无向边
	 * @return 满足条件的好路径总数
	 * @throws NullPointerException 当输入数组为null时抛出异常
	 * @throws IllegalArgumentException 当输入参数无效时抛出异常
	 * 
	 * 算法核心步骤：
	 * 1. 初始化并查集，每个节点自身构成一个集合
	 * 2. 初始路径数目为n（每个节点自身一条路径）
	 * 3. 按照边连接的两个节点的最大值从小到大排序
	 * 4. 依次处理每条边，合并对应的集合，并累加新增的好路径数目
	 */
	public static int numberOfGoodPaths(int[] vals, int[][] edges) {
		// 参数验证
		if (vals == null) {
			throw new NullPointerException("节点值数组不能为null");
		}
		if (edges == null) {
			throw new NullPointerException("边数组不能为null");
		}
		
		int n = vals.length;
		// 处理边界情况
		if (n == 0) {
			return 0; // 空树，没有路径
		}
		if (n == 1) {
			return 1; // 只有一个节点，只有一条路径（自身）
		}
		
		// 验证边的合法性
		for (int[] edge : edges) {
			if (edge.length != 2) {
				throw new IllegalArgumentException("边格式无效");
			}
			if (edge[0] < 0 || edge[0] >= n || edge[1] < 0 || edge[1] >= n) {
				throw new IndexOutOfBoundsException("边中的节点编号超出范围");
			}
		}
		
		// 初始化并查集
		build(n);
		
		// 初始时每个节点自身就是一条好路径，共n条
		int totalPaths = n;
		
		// 按照边连接的两个节点的最大值从小到大排序
		// 这是算法的关键步骤，确保在处理边时，连接的两个集合中的所有节点值都不超过该边的最大值
		Arrays.sort(edges, (e1, e2) -> {
			int max1 = Math.max(vals[e1[0]], vals[e1[1]]);
			int max2 = Math.max(vals[e2[0]], vals[e2[1]]);
			return Integer.compare(max1, max2);
		});
		
		// 依次处理每条边，累加新增的好路径数目
		for (int[] edge : edges) {
			int u = edge[0];
			int v = edge[1];
			// 合并u和v所在的集合，并获取新增的好路径数目
			totalPaths += union(u, v, vals);
		}
		
		return totalPaths;
	}

	/**
	 * 测试方法
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		// 测试用例1：标准情况
		testCase1();
		
		// 测试用例2：复杂树结构
		testCase2();
		
		// 测试用例3：所有节点值相同
		testCase3();
		
		// 测试用例4：所有节点值互不相同
		testCase4();
		
		// 测试用例5：单节点树
		testCase5();
		
		// 测试用例6：小型树结构
		testCase6();
	}
	
	/**
	 * 测试用例1：标准情况
	 * 包含重复节点值和不同层次的树结构
	 */
	private static void testCase1() {
		System.out.println("测试用例1：标准情况");
		// 节点值: [2, 1, 1, 2, 2, 1, 1, 2]
		int[] vals = { 2, 1, 1, 2, 2, 1, 1, 2 };
		int[][] edges = { 
			{ 0, 1 },
			{ 0, 2 },
			{ 1, 3 },
			{ 2, 4 },
			{ 2, 5 },
			{ 5, 6 },
			{ 6, 7 } };
		int expected = 9;
		int result = numberOfGoodPaths(vals, edges);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		System.out.println("  测试" + (result == expected ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例2：复杂树结构
	 * 具有多个分支和不同值分布的树
	 */
	private static void testCase2() {
		System.out.println("测试用例2：复杂树结构");
		// 节点值: [1, 2, 2, 3, 1, 2, 2, 1, 1, 3, 3, 3, 3]
		int[] vals = { 1, 2, 2, 3, 1, 2, 2, 1, 1, 3, 3, 3, 3 };
		int[][] edges = {
			{ 0, 1 },
			{ 0, 2 },
			{ 0, 3 },
			{ 1, 4 },
			{ 4, 7 },
			{ 4, 8 },
			{ 3, 5 },
			{ 3, 6 },
			{ 6, 9 },
			{ 6, 10 },
			{ 6, 11 },
			{ 9, 12 } };
		int expected = 37;
		int result = numberOfGoodPaths(vals, edges);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		System.out.println("  测试" + (result == expected ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例3：所有节点值相同
	 * 此时好路径数目应该是n*(n+1)/2
	 */
	private static void testCase3() {
		System.out.println("测试用例3：所有节点值相同");
		int n = 5;
		int[] vals = new int[n];
		Arrays.fill(vals, 5); // 所有节点值都为5
		
		// 构建一个链式结构的树
		int[][] edges = new int[n-1][2];
		for (int i = 0; i < n-1; i++) {
			edges[i][0] = i;
			edges[i][1] = i+1;
		}
		
		int expected = n * (n + 1) / 2; // 所有可能的路径都是好路径
		int result = numberOfGoodPaths(vals, edges);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		System.out.println("  测试" + (result == expected ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例4：所有节点值互不相同
	 * 此时只有单节点路径是好路径
	 */
	private static void testCase4() {
		System.out.println("测试用例4：所有节点值互不相同");
		int[] vals = { 1, 2, 3, 4, 5 };
		int[][] edges = {
			{ 0, 1 },
			{ 1, 2 },
			{ 2, 3 },
			{ 3, 4 }
		};
		int expected = vals.length; // 只有单节点路径
		int result = numberOfGoodPaths(vals, edges);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		System.out.println("  测试" + (result == expected ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例5：单节点树
	 * 边界情况测试
	 */
	private static void testCase5() {
		System.out.println("测试用例5：单节点树");
		int[] vals = { 42 };
		int[][] edges = {}; // 没有边
		int expected = 1; // 只有一个节点，自身构成一条路径
		int result = numberOfGoodPaths(vals, edges);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		System.out.println("  测试" + (result == expected ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例6：小型树结构
	 * 用于手动验证算法正确性
	 */
	private static void testCase6() {
		System.out.println("测试用例6：小型树结构");
		int[] vals = { 2, 2, 2 };
		int[][] edges = {
			{ 0, 1 },
			{ 1, 2 }
		};
		// 所有可能的路径：
		// 单节点路径: 3条
		// 节点0到1: 1条
		// 节点1到2: 1条
		// 节点0到2: 1条
		// 总共有6条好路径
		int expected = 6;
		int result = numberOfGoodPaths(vals, edges);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		System.out.println("  测试" + (result == expected ? "通过" : "失败"));
		System.out.println();
	}
}

/* C++ 实现
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
private:
    const int MAXN = 30001;
    vector<int> father;
    vector<int> maxcnt;
    
    // 初始化并查集
    void build(int n) {
        father.resize(n);
        maxcnt.resize(n, 1);
        for (int i = 0; i < n; ++i) {
            father[i] = i;
        }
    }
    
    // 查找操作，带路径压缩
    int find(int i) {
        if (father[i] != i) {
            father[i] = find(father[i]);
        }
        return father[i];
    }
    
    // 合并操作
    int unite(int x, int y, const vector<int>& vals) {
        int fx = find(x);
        int fy = find(y);
        int newPaths = 0;
        
        if (vals[fx] > vals[fy]) {
            father[fy] = fx;
        } else if (vals[fx] < vals[fy]) {
            father[fx] = fy;
        } else {
            newPaths = maxcnt[fx] * maxcnt[fy];
            father[fy] = fx;
            maxcnt[fx] += maxcnt[fy];
        }
        return newPaths;
    }
    
public:
    int numberOfGoodPaths(vector<int>& vals, vector<vector<int>>& edges) {
        int n = vals.size();
        build(n);
        
        int totalPaths = n;
        
        // 按照边连接的两个节点的最大值从小到大排序
        sort(edges.begin(), edges.end(), [&vals](const vector<int>& e1, const vector<int>& e2) {
            int max1 = max(vals[e1[0]], vals[e1[1]]);
            int max2 = max(vals[e2[0]], vals[e2[1]]);
            return max1 < max2;
        });
        
        // 处理每条边
        for (const auto& edge : edges) {
            totalPaths += unite(edge[0], edge[1], vals);
        }
        
        return totalPaths;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> vals1 = {2, 1, 1, 2, 2, 1, 1, 2};
    vector<vector<int>> edges1 = {
        {0, 1}, {0, 2}, {1, 3}, {2, 4}, {2, 5}, {5, 6}, {6, 7}
    };
    cout << "测试用例1结果: " << solution.numberOfGoodPaths(vals1, edges1) << endl; // 预期输出: 9
    
    // 测试用例2
    vector<int> vals2 = {1, 2, 2, 3, 1, 2, 2, 1, 1, 3, 3, 3, 3};
    vector<vector<int>> edges2 = {
        {0, 1}, {0, 2}, {0, 3}, {1, 4}, {4, 7}, {4, 8}, {3, 5}, 
        {3, 6}, {6, 9}, {6, 10}, {6, 11}, {9, 12}
    };
    cout << "测试用例2结果: " << solution.numberOfGoodPaths(vals2, edges2) << endl; // 预期输出: 37
    
    return 0;
}
*/

/* Python 实现
class Solution:
    def numberOfGoodPaths(self, vals, edges):
        """
        计算好路径的总数
        
        Args:
            vals: 节点值列表
            edges: 边列表，每个元素为 [u, v]
            
        Returns:
            好路径的总数
        """
        n = len(vals)
        # 初始化并查集
        self.parent = list(range(n))
        # maxcnt[i]表示以i为根的集合中最大值的出现次数
        self.maxcnt = [1] * n
        
        # 按照边连接的两个节点的最大值从小到大排序
        edges.sort(key=lambda e: max(vals[e[0]], vals[e[1]]))
        
        # 初始时有n条路径（每个节点自身）
        total_paths = n
        
        # 处理每条边
        for u, v in edges:
            total_paths += self._union(u, v, vals)
        
        return total_paths
    
    def _find(self, x):
        """
        查找操作，带路径压缩
        
        Args:
            x: 要查找的节点
            
        Returns:
            节点x所在集合的根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self._find(self.parent[x])
        return self.parent[x]
    
    def _union(self, x, y, vals):
        """
        合并操作
        
        Args:
            x: 第一个节点
            y: 第二个节点
            vals: 节点值列表
            
        Returns:
            合并产生的新好路径数目
        """
        fx = self._find(x)
        fy = self._find(y)
        new_paths = 0
        
        if vals[fx] > vals[fy]:
            self.parent[fy] = fx
        elif vals[fx] < vals[fy]:
            self.parent[fx] = fy
        else:
            # 两个集合的最大值相同，产生新路径
            new_paths = self.maxcnt[fx] * self.maxcnt[fy]
            self.parent[fy] = fx
            self.maxcnt[fx] += self.maxcnt[fy]
        
        return new_paths

# 测试代码
solution = Solution()

# 测试用例1
vals1 = [2, 1, 1, 2, 2, 1, 1, 2]
edges1 = [
    [0, 1], [0, 2], [1, 3], [2, 4], [2, 5], [5, 6], [6, 7]
]
print("测试用例1结果:", solution.numberOfGoodPaths(vals1, edges1))  # 预期输出: 9

# 测试用例2
vals2 = [1, 2, 2, 3, 1, 2, 2, 1, 1, 3, 3, 3, 3]
edges2 = [
    [0, 1], [0, 2], [0, 3], [1, 4], [4, 7], [4, 8], [3, 5], 
    [3, 6], [6, 9], [6, 10], [6, 11], [9, 12]
]
print("测试用例2结果:", solution.numberOfGoodPaths(vals2, edges2))  # 预期输出: 37
*/
