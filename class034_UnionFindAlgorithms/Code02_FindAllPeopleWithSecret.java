package class057;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 找出知晓秘密的所有专家
 * 给你一个整数 n ，表示有 n 个专家从 0 到 n - 1 编号
 * 另外给你一个下标从 0 开始的二维整数数组 meetings
 * 其中 meetings[i] = [xi, yi, timei] 表示专家 xi 和专家 yi 在时间 timei 要开一场会
 * 一个专家可以同时参加 多场会议 。最后，给你一个整数 firstPerson
 * 专家 0 有一个 秘密 ，最初，他在时间 0 将这个秘密分享给了专家 firstPerson
 * 接着，这个秘密会在每次有知晓这个秘密的专家参加会议时进行传播
 * 更正式的表达是，每次会议，如果专家 xi 在时间 timei 时知晓这个秘密
 * 那么他将会与专家 yi 分享这个秘密，反之亦然。秘密共享是 瞬时发生 的
 * 也就是说，在同一时间，一个专家不光可以接收到秘密，还能在其他会议上与其他专家分享
 * 在所有会议都结束之后，返回所有知晓这个秘密的专家列表
 * 你可以按 任何顺序 返回答案
 * 
 * 测试链接 : https://leetcode.cn/problems/find-all-people-with-secret/
 * 
 * 算法思路：
 * 1. 使用并查集来管理专家之间的关系
 * 2. 将会议按时间排序，确保按照时间顺序处理
 * 3. 对于同一时间的所有会议，先将参会专家合并到同一集合中
 * 4. 然后检查哪些集合知道秘密，将不知道秘密的专家重置为独立集合
 * 5. 最后收集所有属于知道秘密集合的专家
 * 
 * 算法思路深度解析：
 * - 该问题的关键是处理时间依赖的连通性问题，秘密只能在特定时间点之后传播
 * - 传统的并查集无法直接处理时间维度，因此我们采用按时间分组处理的策略
 * - 对于同一时间点的所有会议，我们先将专家连通，然后检查秘密传播
 * - 对于不知道秘密的专家，我们需要「重置」他们的连通性，以防止秘密在时间上逆向传播
 * - 这是一种模拟「可撤销并查集」的简化实现，但在这个特定问题中足够高效
 * 
 * 时间复杂度分析：
 * - 会议排序: O(m*log(m))，其中m是会议数量
 * - 并查集操作: O(m*α(n))，α是阿克曼函数的反函数，在实际应用中可视为常数
 * - 收集答案: O(n)，n是专家数量
 * - 总体时间复杂度: O(m*log(m) + n + m*α(n)) = O(m*log(m))
 * 
 * 空间复杂度分析：
 * - 并查集数组: O(n)
 * - 秘密标记数组: O(n)
 * - 结果列表: O(n)
 * - 总体空间复杂度: O(n)
 * 
 * 是否为最优解：是，该方法有效处理了时间依赖的连通性问题，时间复杂度主要由排序决定
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查输入参数的有效性
 *    - 处理边界情况（如没有会议、只有一个专家等）
 * 2. 可扩展性：
 *    - 可以轻松扩展处理更复杂的传播规则
 *    - 可以调整MAXN常量以适应不同规模的数据
 * 3. 内存优化：
 *    - 通过MAXN常量预分配数组空间
 *    - 使用静态数组避免频繁的内存分配
 * 4. 线程安全：
 *    - 当前实现不是线程安全的
 *    - 在多线程环境中需要添加同步机制或使用线程本地存储
 * 5. 代码可维护性：
 *    - 使用清晰的命名和详细的注释
 *    - 函数模块化，便于理解和修改
 * 
 * 与其他算法的对比：
 * 1. 并查集 vs 图遍历（如BFS/DFS）：
 *    - 图遍历需要按时间顺序维护状态，实现复杂
 *    - 并查集结合时间分组的方法更加直观高效
 * 2. 并查集优化：
 *    - 路径压缩优化find操作
 *    - 当前实现未使用按秩合并，但已足够高效
 * 3. 与真正的可撤销并查集对比：
 *    - 真正的可撤销并查集需要更复杂的数据结构（如栈）记录操作历史
 *    - 本方法通过特定的重置操作模拟撤销，在这个问题场景下更加高效
 * 
 * 极端情况分析：
 * 1. 没有会议：只有专家0和firstPerson知道秘密
 * 2. 所有会议在同一时间：相当于普通的连通性问题
 * 3. 每个专家只参加一个会议：需要正确处理时间顺序和重置
 * 4. 所有专家都连接在一起：最终所有专家都知道秘密
 * 5. 专家之间的连接形成多个独立的连通分量：只有与专家0所在连通分量有关的专家知道秘密
 * 
 * 性能优化策略：
 * 1. 使用路径压缩优化并查集的find操作
 * 2. 按时间批次处理会议，减少不必要的操作
 * 3. 只在集合的代表元素上维护秘密状态，节省空间和计算
 * 4. 对会议进行排序，确保按时间顺序处理
 * 5. 预处理会议时间范围，快速定位同一时间的会议
 * 
 * 调试技巧：
 * 1. 打印每个时间点的连通状态和秘密传播情况
 * 2. 检查重置操作是否正确执行
 * 3. 验证集合代表元素的秘密状态是否正确维护
 * 4. 对于小规模测试用例，可以手动模拟算法执行过程
 * 
 * 问题迁移能力：
 * 掌握此问题后，可以解决类似的时序连通性问题，如：
 * - 时序网络中的信息传播
 * - 带有时间约束的可达性分析
 * - 动态网络中的社区检测
 * - 基于时间的推荐系统分析
 * 
 * 与机器学习的联系：
 * 该问题模拟了信息传播过程，类似于社交网络中的信息扩散模型，
 * 可应用于病毒传播分析、舆情监测等领域，是许多机器学习模型的基础问题之一
 */
public class Code02_FindAllPeopleWithSecret {

	// 最大专家数量限制，可根据题目约束调整
	public static int MAXN = 100001;

	// 并查集父节点数组，存储每个专家所属集合的代表元素
	public static int[] father = new int[MAXN];

	// 标记数组，记录每个集合是否知道秘密
	// 重要：只有集合的代表元素（根节点）的标记有效
	public static boolean[] secret = new boolean[MAXN];

	/**
	 * 初始化并查集和秘密状态
	 * 
	 * @param n 专家数量
	 * @param first 第一个获知秘密的专家
	 * @throws IllegalArgumentException 当参数无效时抛出异常
	 * @throws ArrayIndexOutOfBoundsException 当专家数量超过MAXN时抛出异常
	 */
	public static void build(int n, int first) {
		if (n <= 0) {
			throw new IllegalArgumentException("专家数量必须大于0");
		}
		if (n > MAXN) {
			throw new ArrayIndexOutOfBoundsException("专家数量超过最大限制");
		}
		if (first < 0 || first >= n) {
			throw new IllegalArgumentException("第一个获知秘密的专家编号无效");
		}
		
		// 初始化每个专家为独立集合
		for (int i = 0; i < n; i++) {
			father[i] = i;       // 每个专家初始时是自己的父节点
			secret[i] = false;   // 初始时都不知道秘密
		}
		
		// 专家0知道秘密，并分享给first专家
		father[first] = 0;     // 将first专家合并到专家0的集合中
		secret[0] = true;      // 只有集合代表节点（专家0）知道秘密
	}

	/**
	 * 查找操作，带路径压缩优化
	 * 
	 * @param i 要查找的专家编号
	 * @return 专家i所在集合的代表元素（根节点）
	 * @throws ArrayIndexOutOfBoundsException 当专家编号超出范围时抛出异常
	 */
	public static int find(int i) {
		if (i < 0 || i >= MAXN) {
			throw new ArrayIndexOutOfBoundsException("专家编号超出范围");
		}
		
		// 路径压缩：将查找路径上的所有节点直接连接到根节点
		// 这大大减少了后续查找操作的时间复杂度
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

	/**
	 * 合并操作，同时维护秘密标记
	 * 
	 * @param x 第一个专家编号
	 * @param y 第二个专家编号
	 * @return 如果合并成功（两个专家原本不在同一集合）返回true，否则返回false
	 */
	public static boolean union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (fx != fy) {
			// 将fx所在集合合并到fy所在集合
			father[fx] = fy;
			// 如果fx所在集合知道秘密，则合并后的集合也知道秘密
			secret[fy] |= secret[fx];
			return true;
		}
		// 如果已经在同一集合中，无需合并
		return false;
	}

	/**
	 * 找出所有知晓秘密的专家
	 * 
	 * @param n 专家数量
	 * @param meetings 会议信息数组，每个元素为 [x, y, time]
	 * @param first 第一个获知秘密的专家
	 * @return 知晓秘密的专家列表
	 * @throws NullPointerException 当meetings为null时抛出异常
	 * @throws IllegalArgumentException 当参数无效时抛出异常
	 * 
	 * 算法核心步骤：
	 * 1. 初始化并查集，专家0和firstPerson初始知道秘密
	 * 2. 将会议按时间排序，确保按时间顺序处理
	 * 3. 按时间批次处理会议：
	 *    a. 首先将同一时间所有会议的参与者合并到同一集合
	 *    b. 然后重置不知道秘密的专家的连通性
	 * 4. 收集所有知道秘密的专家并返回
	 */
	public static List<Integer> findAllPeople(int n, int[][] meetings, int first) {
		// 参数验证
		if (meetings == null) {
			throw new NullPointerException("会议数组不能为null");
		}
		if (n <= 0) {
			throw new IllegalArgumentException("专家数量必须大于0");
		}
		if (first < 0 || first >= n) {
			throw new IllegalArgumentException("第一个获知秘密的专家编号无效");
		}
		
		// 处理边界情况
		if (n == 1) {
			// 只有一个专家（专家0），他当然知道秘密
			List<Integer> singleResult = new ArrayList<>(1);
			singleResult.add(0);
			return singleResult;
		}
		
		// 初始化并查集
		build(n, first);
		
		// 按照会议时间排序
		// 这是算法的关键步骤，确保我们按时间顺序处理会议
		Arrays.sort(meetings, (a, b) -> Integer.compare(a[2], b[2]));
		
		int m = meetings.length;
		// 按时间批次处理会议
		for (int l = 0, r; l < m;) {
			// 找到当前时间的所有会议（从l到r）
			r = l;
			while (r + 1 < m && meetings[l][2] == meetings[r + 1][2]) {
				r++;
			}
			
			// 第一阶段：合并当前时间所有会议的参与者
			// 这模拟了在同一时间点，多个会议并行进行，秘密可以在同一时间点的会议间传播
			for (int i = l; i <= r; i++) {
				union(meetings[i][0], meetings[i][1]);
			}
			
			// 第二阶段：重置不知道秘密的专家
			// 这是一个关键步骤，确保秘密不会逆向传播到之前的时间点
			// 注意：这里不是真正的可撤销并查集，只是将不知道秘密的专家重置为独立集合
			for (int i = l; i <= r; i++) {
				int a = meetings[i][0];
				int b = meetings[i][1];
				
				// 如果a所在集合不知道秘密，重置a为独立集合
				// 这意味着在后续的时间点，a需要重新连接才能共享秘密
				if (!secret[find(a)]) {
					father[a] = a;
				}
				
				// 如果b所在集合不知道秘密，重置b为独立集合
				if (!secret[find(b)]) {
					father[b] = b;
				}
			}
			
			// 处理下一个时间点的会议
			l = r + 1;
		}
		
		// 收集所有知道秘密的专家
		List<Integer> ans = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			// 检查专家i所在集合的代表元素是否知道秘密
			if (secret[find(i)]) {
				ans.add(i);
			}
		}
		return ans;
	}

	/**
	 * 测试方法
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		// 测试用例1：标准情况
		testCase1();
		
		// 测试用例2：复杂时间依赖情况
		testCase2();
		
		// 测试用例3：没有会议的情况
		testCase3();
		
		// 测试用例4：所有专家都在同一时间开会
		testCase4();
		
		// 测试用例5：只有两个专家
		testCase5();
	}
	
	/**
	 * 测试用例1：标准情况
	 * 多个会议按时间顺序进行，秘密逐步传播
	 */
	private static void testCase1() {
		System.out.println("测试用例1：标准情况");
		int n = 6;
		int[][] meetings = {{1, 2, 5}, {2, 3, 8}, {1, 5, 10}};
		int first = 1;
		List<Integer> expected = Arrays.asList(0, 1, 2, 3, 5);
		List<Integer> result = findAllPeople(n, meetings, first);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		// 由于顺序可能不同，我们需要排序后比较
		result.sort(null);
		expected.sort(null);
		System.out.println("  测试" + (result.equals(expected) ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例2：复杂时间依赖情况
	 * 会议有重叠的时间，需要正确处理同一时间点的秘密传播
	 */
	private static void testCase2() {
		System.out.println("测试用例2：复杂时间依赖情况");
		int n = 4;
		int[][] meetings = {{3, 1, 3}, {1, 2, 2}, {0, 3, 3}};
		int first = 3;
		List<Integer> expected = Arrays.asList(0, 1, 3);
		List<Integer> result = findAllPeople(n, meetings, first);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		// 由于顺序可能不同，我们需要排序后比较
		result.sort(null);
		expected.sort(null);
		System.out.println("  测试" + (result.equals(expected) ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例3：没有会议的情况
	 * 只有专家0和firstPerson知道秘密
	 */
	private static void testCase3() {
		System.out.println("测试用例3：没有会议的情况");
		int n = 5;
		int[][] meetings = {};
		int first = 2;
		List<Integer> expected = Arrays.asList(0, 2);
		List<Integer> result = findAllPeople(n, meetings, first);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		// 由于顺序可能不同，我们需要排序后比较
		result.sort(null);
		expected.sort(null);
		System.out.println("  测试" + (result.equals(expected) ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例4：所有专家都在同一时间开会
	 * 相当于普通的连通性问题
	 */
	private static void testCase4() {
		System.out.println("测试用例4：所有专家都在同一时间开会");
		int n = 5;
		int[][] meetings = {{0, 1, 5}, {1, 2, 5}, {2, 3, 5}, {3, 4, 5}};
		int first = 0; // 专家0知道秘密，first也是0表示没有其他初始知情者
		List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4); // 所有专家都知道秘密
		List<Integer> result = findAllPeople(n, meetings, first);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		// 由于顺序可能不同，我们需要排序后比较
		result.sort(null);
		expected.sort(null);
		System.out.println("  测试" + (result.equals(expected) ? "通过" : "失败"));
		System.out.println();
	}
	
	/**
	 * 测试用例5：只有两个专家
	 * 边界情况测试
	 */
	private static void testCase5() {
		System.out.println("测试用例5：只有两个专家");
		int n = 2;
		int[][] meetings = {{0, 1, 10}};
		int first = 1;
		List<Integer> expected = Arrays.asList(0, 1); // 两个专家都知道秘密
		List<Integer> result = findAllPeople(n, meetings, first);
		System.out.println("  结果: " + result);
		System.out.println("  预期: " + expected);
		// 由于顺序可能不同，我们需要排序后比较
		result.sort(null);
		expected.sort(null);
		System.out.println("  测试" + (result.equals(expected) ? "通过" : "失败"));
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
    const int MAXN = 100001;
    vector<int> father;
    vector<bool> secret;
    
    // 初始化并查集
    void build(int n, int first) {
        father.resize(n);
        secret.resize(n, false);
        
        for (int i = 0; i < n; ++i) {
            father[i] = i;
        }
        father[first] = 0;
        secret[0] = true;
    }
    
    // 查找操作，带路径压缩
    int find(int i) {
        if (father[i] != i) {
            father[i] = find(father[i]);
        }
        return father[i];
    }
    
    // 合并操作
    void unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx != fy) {
            father[fx] = fy;
            secret[fy] = secret[fy] || secret[fx];
        }
    }
    
public:
    vector<int> findAllPeople(int n, vector<vector<int>>& meetings, int firstPerson) {
        // 初始化并查集
        build(n, firstPerson);
        
        // 按照会议时间排序
        sort(meetings.begin(), meetings.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[2] < b[2];
        });
        
        int m = meetings.size();
        for (int l = 0, r; l < m;) {
            // 找到当前时间的所有会议
            r = l;
            while (r + 1 < m && meetings[l][2] == meetings[r + 1][2]) {
                r++;
            }
            
            // 合并当前时间所有会议的参与者
            for (int i = l; i <= r; ++i) {
                unite(meetings[i][0], meetings[i][1]);
            }
            
            // 重置不知道秘密的专家
            for (int i = l; i <= r; ++i) {
                int a = meetings[i][0];
                int b = meetings[i][1];
                if (!secret[find(a)]) {
                    father[a] = a;
                }
                if (!secret[find(b)]) {
                    father[b] = b;
                }
            }
            
            l = r + 1;
        }
        
        // 收集结果
        vector<int> result;
        for (int i = 0; i < n; ++i) {
            if (secret[find(i)]) {
                result.push_back(i);
            }
        }
        return result;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 6;
    vector<vector<int>> meetings1 = {{1, 2, 5}, {2, 3, 8}, {1, 5, 10}};
    int first1 = 1;
    vector<int> result1 = solution.findAllPeople(n1, meetings1, first1);
    cout << "测试用例1结果: [";
    for (size_t i = 0; i < result1.size(); ++i) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例2
    int n2 = 4;
    vector<vector<int>> meetings2 = {{3, 1, 3}, {1, 2, 2}, {0, 3, 3}};
    int first2 = 3;
    vector<int> result2 = solution.findAllPeople(n2, meetings2, first2);
    cout << "测试用例2结果: [";
    for (size_t i = 0; i < result2.size(); ++i) {
        cout << result2[i];
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    return 0;
}
*/

/* Python 实现
class Solution:
    def findAllPeople(self, n, meetings, firstPerson):
        """
        找出所有知晓秘密的专家
        
        Args:
            n: 专家数量
            meetings: 会议信息列表，每个元素为 [x, y, time]
            firstPerson: 第一个获知秘密的专家
            
        Returns:
            知晓秘密的专家列表
        """
        # 初始化并查集
        self.parent = list(range(n))
        # secret数组只在根节点有效
        self.secret = [False] * n
        # 专家0知道秘密
        self.secret[0] = True
        # 专家0和firstPerson属于同一集合
        self.parent[firstPerson] = 0
        
        # 按会议时间排序
        meetings.sort(key=lambda x: x[2])
        
        m = len(meetings)
        l = 0
        while l < m:
            # 找到当前时间的所有会议
            r = l
            while r + 1 < m and meetings[l][2] == meetings[r + 1][2]:
                r += 1
            
            # 合并当前时间所有会议的参与者
            for i in range(l, r + 1):
                x, y, _ = meetings[i]
                self.union(x, y)
            
            # 重置不知道秘密的专家
            for i in range(l, r + 1):
                x, y, _ = meetings[i]
                if not self.secret[self.find(x)]:
                    self.parent[x] = x
                if not self.secret[self.find(y)]:
                    self.parent[y] = y
            
            l = r + 1
        
        # 收集结果
        result = []
        for i in range(n):
            if self.secret[self.find(i)]:
                result.append(i)
        return result
    
    def find(self, x):
        """
        查找操作，带路径压缩
        
        Args:
            x: 要查找的节点
            
        Returns:
            节点x所在集合的根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并操作
        
        Args:
            x: 第一个节点
            y: 第二个节点
        """
        fx = self.find(x)
        fy = self.find(y)
        if fx != fy:
            # 将fx所在集合合并到fy所在集合
            self.parent[fx] = fy
            # 更新秘密状态
            self.secret[fy] = self.secret[fy] or self.secret[fx]

# 测试代码
solution = Solution()

# 测试用例1
n1 = 6
meetings1 = [[1, 2, 5], [2, 3, 8], [1, 5, 10]]
first1 = 1
result1 = solution.findAllPeople(n1, meetings1, first1)
print("测试用例1结果:", result1)  # 预期输出: [0, 1, 2, 3, 5]

# 测试用例2
n2 = 4
meetings2 = [[3, 1, 3], [1, 2, 2], [0, 3, 3]]
first2 = 3
result2 = solution.findAllPeople(n2, meetings2, first2)
print("测试用例2结果:", result2)  # 预期输出: [0, 1, 3]
*/
