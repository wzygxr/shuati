package class126;

// 轮廓线DP专题 - 题目1: 网格涂色问题
// 用三种不同颜色为网格涂色
// 给你两个整数m和n，表示m*n的网格，其中每个单元格最开始是白色
// 请你用红、绿、蓝三种颜色为每个单元格涂色，所有单元格都需要被涂色
// 要求相邻单元格的颜色一定要不同
// 返回网格涂色的方法数，答案对 1000000007 取模
// 1 <= m <= 5
// 1 <= n <= 1000
// 测试链接 : https://leetcode.cn/problems/painting-a-grid-with-three-different-colors/
// 这是一个典型的轮廓线DP问题，使用三进制表示每一行的颜色状态
//
// 题目大意：
// 给定一个m×n的网格，要求用三种颜色为每个单元格涂色，使得相邻单元格颜色不同。
// 求满足条件的涂色方案数。
//
// 解题思路：
// 使用轮廓线DP，逐格处理，记录每个位置的颜色状态。
// 状态表示：用三进制表示轮廓线状态，0表示红色，1表示绿色，2表示蓝色。
// 状态转移：考虑当前格子的颜色，确保与上方和左侧格子颜色不同。
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code02_GridPainting.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code02_GridPainting.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code02_GridPainting.py

public class Code02_GridPainting {

	// 最大行数或列数
	public static int MAXN = 1001;

	// 最大列数或行数（较小的那个维度）
	public static int MAXM = 5;

	// 最大状态数，使用三进制表示每行的颜色
	public static int MAXS = (int) Math.pow(3, MAXM);

	// 取模值
	public static int MOD = 1000000007;

	// 存储行数或列数（较大的那个维度）
	public static int n;

	// 存储列数或行数（较小的那个维度）
	public static int m;

	// 实际最大状态数
	public static int maxs;

	// 动态规划数组：dp[i][j][s] 表示处理到第i行第j列时，状态为s的方案数
	// 其中状态s使用三进制表示当前行已处理部分的颜色
	public static int[][][] dp = new int[MAXN][MAXM][MAXS];

	// 存储第一行的所有有效状态
	public static int[] first = new int[MAXS];

	// 第一行有效状态的数量
	public static int size;

	/**
	 * 计算网格涂色的可能方案数
	 * 
	 * @param rows 网格的行数
	 * @param cols 网格的列数
	 * @return 所有可能的涂色方案数
	 */
	public static int colorTheGrid(int rows, int cols) {
		// 初始化数据结构
		build(rows, cols);
		int ans = 0;
		// 遍历所有可能的第一行状态，累加结果
		for (int i = 0; i < size; i++) {
			ans = (ans + f(1, 0, first[i], 1)) % MOD;
		}
		return ans;
	}

	/**
	 * 初始化数据结构，准备动态规划
	 * 
	 * @param rows 网格的行数
	 * @param cols 网格的列数
	 */
	public static void build(int rows, int cols) {
		// 为了优化计算，将较大的维度作为行，较小的维度作为列
		n = Math.max(rows, cols);
		m = Math.min(rows, cols);
		// 计算最大状态数
		maxs = (int) Math.pow(3, m);
		// 初始化DP数组为-1（表示未计算）
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int s = 0; s < maxs; s++) {
					dp[i][j][s] = -1;
				}
			}
		}
		// 重置状态数量
		size = 0;
		// 生成所有有效的第一行状态
		dfs(0, 0, 1);
	}

	/**
	 * 使用深度优先搜索生成所有有效的第一行状态
	 * 
	 * @param j 当前处理的列索引
	 * @param s 当前状态（三进制表示）
	 * @param bit 当前位的权重（3的幂）
	 */
	// 取得所有第一行的有效状态
	public static void dfs(int j, int s, int bit) {
		if (j == m) {
			// 处理完一行，保存该状态
			first[size++] = s;
		} else {
			// 获取左侧格子的颜色（如果有的话）
			int left = j == 0 ? -1 : get(s, bit / 3);
			// 尝试三种颜色，确保不与左侧颜色相同
			if (left != 0) {
				dfs(j + 1, set(s, bit, 0), bit * 3);
			}
			if (left != 1) {
				dfs(j + 1, set(s, bit, 1), bit * 3);
			}
			if (left != 2) {
				dfs(j + 1, set(s, bit, 2), bit * 3);
			}
		}
	}

	/**
	 * 动态规划计算方案数
	 * 
	 * @param i 当前处理的行索引
	 * @param j 当前处理的列索引
	 * @param s 当前状态（三进制表示）
	 * @param bit 当前位的权重（3的幂）
	 * @return 从当前状态到结束的方案数
	 */
	public static int f(int i, int j, int s, int bit) {
		// 已经处理完所有行，返回1种方案
		if (i == n) {
			return 1;
		}
		// 处理完当前行，转到下一行的第一个列
		if (j == m) {
			return f(i + 1, 0, s, 1);
		}
		// 记忆化搜索，如果已经计算过，直接返回结果
		if (dp[i][j][s] != -1) {
			return dp[i][j][s];
		}
		// 上方的颜色（来自上一行同一位置）
		int up = get(s, bit);
		// 左侧的颜色（来自当前行已处理的部分），-1代表左侧没有格子
		int left = j == 0 ? -1 : get(s, bit / 3);
		int ans = 0;
		// 尝试三种颜色，确保不与上方和左侧颜色相同
		if (up != 0 && left != 0) {
			ans = (ans + f(i, j + 1, set(s, bit, 0), bit * 3)) % MOD;
		}
		if (up != 1 && left != 1) {
			ans = (ans + f(i, j + 1, set(s, bit, 1), bit * 3)) % MOD;
		}
		if (up != 2 && left != 2) {
			ans = (ans + f(i, j + 1, set(s, bit, 2), bit * 3)) % MOD;
		}
		// 保存结果并返回
		dp[i][j][s] = ans;
		return ans;
	}

	/**
	 * 从状态中获取指定位的颜色
	 * 
	 * @param s 状态值
	 * @param bit 位权重
	 * @return 颜色值（0、1、2）
	 */
	public static int get(int s, int bit) {
		return s / bit % 3;
	}

	/**
	 * 在状态中设置指定位的颜色
	 * 
	 * @param s 当前状态
	 * @param bit 位权重
	 * @param v 要设置的颜色值
	 * @return 更新后的状态
	 */
	public static int set(int s, int bit, int v) {
		return s - get(s, bit) * bit + v * bit;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1：3x2网格
		System.out.println(colorTheGrid(3, 2));  // 预期输出：36
		
		// 测试用例2：2x3网格
		System.out.println(colorTheGrid(2, 3));  // 预期输出：54
		
		// 测试用例3：5x1网格
		System.out.println(colorTheGrid(5, 1));  // 预期输出：3
	}
}

// 补充题目1：LeetCode 790 Domino and Tromino Tiling
/*
题目描述：
有两种形状的骨牌：
1. 1x2的多米诺骨牌，可以水平或垂直放置
2. 2x3的托米诺骨牌，其形状为 L 形，可以有4种不同的旋转方式

给你一个整数 n ，表示一个 2xn 的网格，可以使用任意数量的多米诺骨牌和托米诺骨牌。
返回铺满整个网格的不同铺法总数。答案需要对 10^9 + 7 取模。

链接：https://leetcode.cn/problems/domino-and-tromino-tiling/

算法解析：
这是一个经典的动态规划问题，我们可以通过定义状态来表示当前网格的覆盖情况。
我们定义四个状态：
- dp[n][0]: 铺满前n列
- dp[n][1]: 铺满前n列，且第n+1列的第一行已铺
- dp[n][2]: 铺满前n列，且第n+1列的第二行已铺
- dp[n][3]: 铺满前n-1列，且第n列已铺

状态转移方程：
dp[n][0] = dp[n-1][0] + dp[n-2][0] + dp[n-1][1] + dp[n-1][2]
dp[n][1] = dp[n-2][0] + dp[n-1][2]
dp[n][2] = dp[n-2][0] + dp[n-1][1]
dp[n][3] = dp[n-1][0]

C++ 实现代码：
class Solution {
public:
    int numTilings(int n) {
        const int MOD = 1e9 + 7;
        vector<vector<long long>> dp(n + 1, vector<long long>(4, 0));
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; ++i) {
            if (i >= 1) {
                dp[i][0] = (dp[i][0] + dp[i-1][0]) % MOD;
                dp[i][3] = (dp[i][3] + dp[i-1][0]) % MOD;
            }
            if (i >= 2) {
                dp[i][0] = (dp[i][0] + dp[i-2][0]) % MOD;
                dp[i][1] = (dp[i][1] + dp[i-2][0]) % MOD;
                dp[i][2] = (dp[i][2] + dp[i-2][0]) % MOD;
            }
            if (i >= 1) {
                dp[i][0] = (dp[i][0] + dp[i-1][1]) % MOD;
                dp[i][0] = (dp[i][0] + dp[i-1][2]) % MOD;
            }
            if (i >= 1) {
                dp[i][1] = (dp[i][1] + dp[i-1][2]) % MOD;
                dp[i][2] = (dp[i][2] + dp[i-1][1]) % MOD;
            }
        }
        
        return dp[n][0];
    }
};

Python 实现代码：
class Solution:
    def numTilings(self, n: int) -> int:
        MOD = 10**9 + 7
        dp = [[0] * 4 for _ in range(n + 1)]
        dp[0][0] = 1
        
        for i in range(1, n + 1):
            if i >= 1:
                dp[i][0] = (dp[i][0] + dp[i-1][0]) % MOD
                dp[i][3] = (dp[i][3] + dp[i-1][0]) % MOD
            if i >= 2:
                dp[i][0] = (dp[i][0] + dp[i-2][0]) % MOD
                dp[i][1] = (dp[i][1] + dp[i-2][0]) % MOD
                dp[i][2] = (dp[i][2] + dp[i-2][0]) % MOD
            if i >= 1:
                dp[i][0] = (dp[i][0] + dp[i-1][1]) % MOD
                dp[i][0] = (dp[i][0] + dp[i-1][2]) % MOD
            if i >= 1:
                dp[i][1] = (dp[i][1] + dp[i-1][2]) % MOD
                dp[i][2] = (dp[i][2] + dp[i-1][1]) % MOD
        
        return dp[n][0]

Java 实现代码：
class Solution {
    public int numTilings(int n) {
        final int MOD = 1000000007;
        long[][] dp = new long[n + 1][4];
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            if (i >= 1) {
                dp[i][0] = (dp[i][0] + dp[i-1][0]) % MOD;
                dp[i][3] = (dp[i][3] + dp[i-1][0]) % MOD;
            }
            if (i >= 2) {
                dp[i][0] = (dp[i][0] + dp[i-2][0]) % MOD;
                dp[i][1] = (dp[i][1] + dp[i-2][0]) % MOD;
                dp[i][2] = (dp[i][2] + dp[i-2][0]) % MOD;
            }
            if (i >= 1) {
                dp[i][0] = (dp[i][0] + dp[i-1][1]) % MOD;
                dp[i][0] = (dp[i][0] + dp[i-1][2]) % MOD;
            }
            if (i >= 1) {
                dp[i][1] = (dp[i][1] + dp[i-1][2]) % MOD;
                dp[i][2] = (dp[i][2] + dp[i-1][1]) % MOD;
            }
        }
        
        return (int) dp[n][0];
    }
}

时间复杂度：O(n)
空间复杂度：O(n)

// 补充题目2：POJ 2411 Mondriaan's Dream
/*
题目描述：
求用1×2的多米诺骨牌铺满n×m的网格的方案数。

链接：http://poj.org/problem?id=2411

算法解析：
这是一个经典的状态压缩动态规划问题。我们使用二进制状态表示每一行的骨牌放置情况。
对于每一行，我们需要确保当前行的状态与上一行的状态兼容，即不能有重叠的骨牌。

C++ 实现代码：
#include <iostream>
#include <cstring>
using namespace std;

typedef long long ll;
ll dp[12][1<<11];
int n, m;

// 检查状态是否合法（没有奇数个连续的0）
bool is_valid(int s) {
    int cnt = 0;
    for (int i = 0; i < m; ++i) {
        if ((s >> i) & 1) {
            cnt = 0;
        } else {
            cnt++;
            if (cnt % 2 != 0) {
                return false;
            }
        }
    }
    return cnt % 2 == 0;
}

// 检查两个状态是否兼容
bool is_compatible(int a, int b) {
    return (a & b) == 0 && is_valid(a | b);
}

int main() {
    while (cin >> n >> m && n && m) {
        memset(dp, 0, sizeof(dp));
        
        // 预处理所有合法的行状态
        for (int s = 0; s < (1 << m); ++s) {
            if (is_valid(s)) {
                dp[0][s] = 1;
            }
        }
        
        // 动态规划填表
        for (int i = 1; i < n; ++i) {
            for (int s = 0; s < (1 << m); ++s) {
                for (int prev = 0; prev < (1 << m); ++prev) {
                    if (is_compatible(prev, s)) {
                        dp[i][s] += dp[i-1][prev];
                    }
                }
            }
        }
        
        cout << dp[n-1][0] << endl;
    }
    return 0;
}

Python 实现代码：
import sys

def main():
    while True:
        line = sys.stdin.readline()
        if not line:
            break
        n, m = map(int, line.strip().split())
        if n == 0 and m == 0:
            break
        
        # 交换行列，使得m较小，优化性能
        if n < m:
            n, m = m, n
        
        dp = [[0] * (1 << m) for _ in range(n)]
        
        # 检查状态是否合法
        def is_valid(s):
            cnt = 0
            for i in range(m):
                if (s >> i) & 1:
                    cnt = 0
                else:
                    cnt += 1
                    if cnt % 2 != 0:
                        return False
            return cnt % 2 == 0
        
        # 预处理第一行
        for s in range(1 << m):
            if is_valid(s):
                dp[0][s] = 1
        
        # 检查两个状态是否兼容
        def is_compatible(a, b):
            return (a & b) == 0 and is_valid(a | b)
        
        # 动态规划
        for i in range(1, n):
            for s in range(1 << m):
                for prev in range(1 << m):
                    if is_compatible(prev, s):
                        dp[i][s] += dp[i-1][prev]
        
        print(dp[n-1][0])

if __name__ == "__main__":
    main()

Java 实现代码：
import java.util.*;

public class Main {
    static long[][] dp;
    static int n, m;
    
    static boolean is_valid(int s) {
        int cnt = 0;
        for (int i = 0; i < m; i++) {
            if ((s >> i & 1) == 1) {
                cnt = 0;
            } else {
                cnt++;
                if (cnt % 2 != 0) {
                    return false;
                }
            }
        }
        return cnt % 2 == 0;
    }
    
    static boolean is_compatible(int a, int b) {
        return (a & b) == 0 && is_valid(a | b);
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            n = sc.nextInt();
            m = sc.nextInt();
            if (n == 0 && m == 0) break;
            
            // 交换行列，优化性能
            if (n < m) {
                int temp = n;
                n = m;
                m = temp;
            }
            
            dp = new long[n][1 << m];
            
            // 预处理第一行
            for (int s = 0; s < (1 << m); s++) {
                if (is_valid(s)) {
                    dp[0][s] = 1;
                }
            }
            
            // 动态规划
            for (int i = 1; i < n; i++) {
                for (int s = 0; s < (1 << m); s++) {
                    for (int prev = 0; prev < (1 << m); prev++) {
                        if (is_compatible(prev, s)) {
                            dp[i][s] += dp[i-1][prev];
                        }
                    }
                }
            }
            
            System.out.println(dp[n-1][0]);
        }
        sc.close();
    }
}

时间复杂度：O(n * 4^m)
空间复杂度：O(n * 2^m)

// 补充题目3：HDU 1693 Eat the Trees
/*
题目描述：
给一个n*m的网格，某些格子是障碍，求用1×2的骨牌覆盖整个非障碍格子的方案数。

链接：http://acm.hdu.edu.cn/showproblem.php?pid=1693

算法解析：
这是一个典型的插头DP问题。我们使用二进制状态表示轮廓线上的插头情况，每个位置有两种可能的状态：
0表示没有插头，1表示有插头。通过状态转移，我们可以计算出所有可能的覆盖方式。

C++ 实现代码：
#include <iostream>
#include <cstring>
#include <vector>
using namespace std;

typedef long long ll;
const int MAXN = 12;
const int MAXM = 12;

ll dp[2][1<<MAXM];
int grid[MAXN][MAXM];
int n, m;

void solve() {
    memset(dp, 0, sizeof(dp));
    int cur = 0, next = 1;
    dp[cur][0] = 1;
    
    for (int i = 0; i < n; ++i) {
        // 换行时，将所有状态左移一位
        for (int j = 0; j < (1 << m); ++j) {
            dp[next][j << 1] = dp[cur][j];
        }
        swap(cur, next);
        memset(dp[next], 0, sizeof(dp[next]));
        
        for (int j = 0; j < m; ++j) {
            for (int s = 0; s < (1 << (m + 1)); ++s) {
                if (dp[cur][s] == 0) continue;
                
                // 当前格子是障碍
                if (grid[i][j] == 1) {
                    if ((s & 1) == 0 && ((s >> m) & 1) == 0) {
                        dp[next][(s >> 1)] += dp[cur][s];
                    }
                    continue;
                }
                
                int left = s & 1;
                int up = (s >> m) & 1;
                
                if (left == 0 && up == 0) {
                    // 新的插头对
                    if (j < m - 1 && grid[i][j+1] == 0) {
                        dp[next][(s >> 1) | (1 << m)] += dp[cur][s];
                    }
                    if (i < n - 1 && grid[i+1][j] == 0) {
                        dp[next][(s >> 1) | 1] += dp[cur][s];
                    }
                } else if (left == 1 && up == 0) {
                    // 右插头延续或向下转
                    if (j < m - 1 && grid[i][j+1] == 0) {
                        dp[next][(s >> 1) | (1 << m)] += dp[cur][s];
                    }
                    if (i < n - 1 && grid[i+1][j] == 0) {
                        dp[next][(s >> 1)] += dp[cur][s];
                    }
                } else if (left == 0 && up == 1) {
                    // 下插头延续或向右转
                    if (i < n - 1 && grid[i+1][j] == 0) {
                        dp[next][(s >> 1) | 1] += dp[cur][s];
                    }
                    if (j < m - 1 && grid[i][j+1] == 0) {
                        dp[next][(s >> 1)] += dp[cur][s];
                    }
                } else if (left == 1 && up == 1) {
                    // 合并两个插头
                    dp[next][(s >> 1)] += dp[cur][s];
                }
            }
            swap(cur, next);
            memset(dp[next], 0, sizeof(dp[next]));
        }
    }
    
    cout << dp[cur][0] << endl;
}

int main() {
    int T, cas = 1;
    cin >> T;
    while (T--) {
        cin >> n >> m;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                cin >> grid[i][j];
            }
        }
        cout << "Case " << cas++ << ": There are " << solve() << " ways to eat the trees." << endl;
    }
    return 0;
}

Python 实现代码：
def main():
    import sys
    T = int(sys.stdin.readline())
    for cas in range(1, T + 1):
        n, m = map(int, sys.stdin.readline().split())
        grid = []
        for _ in range(n):
            grid.append(list(map(int, sys.stdin.readline().split())))
        
        # 初始化DP数组
        dp = [ [0] * (1 << (m + 1)) for _ in range(2) ]
        cur, nxt = 0, 1
        dp[cur][0] = 1
        
        for i in range(n):
            # 换行时左移一位
            for j in range(1 << (m + 1)):
                dp[nxt][j << 1] = dp[cur][j]
            cur, nxt = nxt, cur
            for j in range(1 << (m + 1)):
                dp[nxt][j] = 0
            
            for j in range(m):
                for s in range(1 << (m + 1)):
                    if dp[cur][s] == 0:
                        continue
                    
                    # 障碍格子
                    if grid[i][j] == 1:
                        if (s & 1) == 0 and ((s >> m) & 1) == 0:
                            dp[nxt][(s >> 1)] += dp[cur][s]
                        continue
                    
                    left = s & 1
                    up = (s >> m) & 1
                    
                    if left == 0 and up == 0:
                        # 新的插头对
                        if j < m - 1 and grid[i][j+1] == 0:
                            dp[nxt][(s >> 1) | (1 << m)] += dp[cur][s]
                        if i < n - 1 and grid[i+1][j] == 0:
                            dp[nxt][(s >> 1) | 1] += dp[cur][s]
                    elif left == 1 and up == 0:
                        # 右插头
                        if j < m - 1 and grid[i][j+1] == 0:
                            dp[nxt][(s >> 1) | (1 << m)] += dp[cur][s]
                        if i < n - 1 and grid[i+1][j] == 0:
                            dp[nxt][(s >> 1)] += dp[cur][s]
                    elif left == 0 and up == 1:
                        # 下插头
                        if i < n - 1 and grid[i+1][j] == 0:
                            dp[nxt][(s >> 1) | 1] += dp[cur][s]
                        if j < m - 1 and grid[i][j+1] == 0:
                            dp[nxt][(s >> 1)] += dp[cur][s]
                    elif left == 1 and up == 1:
                        # 合并插头
                        dp[nxt][(s >> 1)] += dp[cur][s]
                
                cur, nxt = nxt, cur
                for j in range(1 << (m + 1)):
                    dp[nxt][j] = 0
        
        print(f"Case {cas}: There are {dp[cur][0]} ways to eat the trees.")

if __name__ == "__main__":
    main()

Java 实现代码：
import java.util.*;

public class Main {
    static long[][] dp;
    static int[][] grid;
    static int n, m;
    
    static long solve() {
        dp = new long[2][1 << (m + 1)];
        int cur = 0, next = 1;
        dp[cur][0] = 1;
        
        for (int i = 0; i < n; i++) {
            // 换行时左移一位
            for (int j = 0; j < (1 << (m + 1)); j++) {
                dp[next][j << 1] = dp[cur][j];
            }
            cur = next;
            next = 1 - cur;
            Arrays.fill(dp[next], 0);
            
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < (1 << (m + 1)); s++) {
                    if (dp[cur][s] == 0) continue;
                    
                    // 障碍格子
                    if (grid[i][j] == 1) {
                        if ((s & 1) == 0 && ((s >> m) & 1) == 0) {
                            dp[next][(s >> 1)] += dp[cur][s];
                        }
                        continue;
                    }
                    
                    int left = s & 1;
                    int up = (s >> m) & 1;
                    
                    if (left == 0 && up == 0) {
                        // 新的插头对
                        if (j < m - 1 && grid[i][j+1] == 0) {
                            dp[next][(s >> 1) | (1 << m)] += dp[cur][s];
                        }
                        if (i < n - 1 && grid[i+1][j] == 0) {
                            dp[next][(s >> 1) | 1] += dp[cur][s];
                        }
                    } else if (left == 1 && up == 0) {
                        // 右插头
                        if (j < m - 1 && grid[i][j+1] == 0) {
                            dp[next][(s >> 1) | (1 << m)] += dp[cur][s];
                        }
                        if (i < n - 1 && grid[i+1][j] == 0) {
                            dp[next][(s >> 1)] += dp[cur][s];
                        }
                    } else if (left == 0 && up == 1) {
                        // 下插头
                        if (i < n - 1 && grid[i+1][j] == 0) {
                            dp[next][(s >> 1) | 1] += dp[cur][s];
                        }
                        if (j < m - 1 && grid[i][j+1] == 0) {
                            dp[next][(s >> 1)] += dp[cur][s];
                        }
                    } else if (left == 1 && up == 1) {
                        // 合并插头
                        dp[next][(s >> 1)] += dp[cur][s];
                    }
                }
                
                cur = next;
                next = 1 - cur;
                Arrays.fill(dp[next], 0);
            }
        }
        
        return dp[cur][0];
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int cas = 1; cas <= T; cas++) {
            n = sc.nextInt();
            m = sc.nextInt();
            grid = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }
            System.out.println("Case " + cas + ": There are " + solve() + " ways to eat the trees.");
        }
        sc.close();
    }
}

时间复杂度：O(n * m * 2^m)
空间复杂度：O(2^m)

// 补充题目4：UVA 10572 Black and White
/*
题目描述：
给一个n*m的网格，每个格子可以是黑色、白色或障碍。
要求相邻的非障碍格子必须有不同的颜色，且黑白色必须相等。
求满足条件的方案数。

链接：https://vjudge.net/problem/UVA-10572

算法解析：
这是一个使用三进制插头DP的问题。每个位置有三种可能的状态：
0表示没有插头，1表示黑色，2表示白色。
我们需要跟踪当前的颜色使用情况，并确保黑白相等。

C++ 实现代码：
#include <iostream>
#include <cstring>
#include <map>
using namespace std;

typedef long long ll;
const int MAXN = 12;
const int MAXM = 12;

int n, m;
int grid[MAXN][MAXM];
map<ll, ll> dp[2];
ll power3[MAXM + 1];

void initPower3() {
    power3[0] = 1;
    for (int i = 1; i <= MAXM; ++i) {
        power3[i] = power3[i-1] * 3;
    }
}

int get(ll s, int pos) {
    return s / power3[pos] % 3;
}

ll set(ll s, int pos, int val) {
    return s - get(s, pos) * power3[pos] + (ll)val * power3[pos];
}

ll solve() {
    int cur = 0, next = 1;
    dp[cur].clear();
    dp[cur][0] = 1;
    
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            dp[next].clear();
            
            for (auto &p : dp[cur]) {
                ll s = p.first;
                ll cnt = p.second;
                
                // 获取当前位置的上方和左方插头
                int up = get(s, j);
                int left = j > 0 ? get(s, j-1) : 0;
                
                // 障碍格子
                if (grid[i][j] == 1) {
                    if (up == 0 && left == 0) {
                        dp[next][s] += cnt;
                    }
                    continue;
                }
                
                // 尝试两种颜色
                for (int color = 1; color <= 2; ++color) {
                    // 检查与上方和左方的颜色冲突
                    if (up == color || left == color) {
                        continue;
                    }
                    
                    // 检查是否满足题目约束
                    if (grid[i][j] != 0 && grid[i][j] != color) {
                        continue;
                    }
                    
                    ll ns = set(s, j, color);
                    if (j > 0) {
                        ns = set(ns, j-1, 0);
                    }
                    dp[next][ns] += cnt;
                }
            }
            
            swap(cur, next);
        }
        
        // 换行处理
        dp[next].clear();
        for (auto &p : dp[cur]) {
            ll s = p.first;
            ll cnt = p.second;
            bool valid = true;
            for (int j = 0; j < m; ++j) {
                if (get(s, j) != 0) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                dp[next][s] += cnt;
            }
        }
        swap(cur, next);
    }
    
    ll ans = 0;
    for (auto &p : dp[cur]) {
        ans += p.second;
    }
    return ans;
}

int main() {
    initPower3();
    int T;
    cin >> T;
    while (T--) {
        cin >> n >> m;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                cin >> grid[i][j];
            }
        }
        cout << solve() << endl;
    }
    return 0;
}

Python 实现代码：
def main():
    import sys
    from collections import defaultdict
    
    # 预计算3的幂次
    power3 = [1] * 13
    for i in range(1, 13):
        power3[i] = power3[i-1] * 3
    
    def get(s, pos):
        return s // power3[pos] % 3
    
    def set_val(s, pos, val):
        return s - get(s, pos) * power3[pos] + val * power3[pos]
    
    T = int(sys.stdin.readline())
    for _ in range(T):
        n, m = map(int, sys.stdin.readline().split())
        grid = []
        for _ in range(n):
            grid.append(list(map(int, sys.stdin.readline().split())))
        
        # 初始化DP
        dp = [defaultdict(int), defaultdict(int)]
        cur, nxt = 0, 1
        dp[cur][0] = 1
        
        for i in range(n):
            for j in range(m):
                dp[nxt].clear()
                
                for s, cnt in dp[cur].items():
                    up = get(s, j)
                    left = get(s, j-1) if j > 0 else 0
                    
                    # 障碍格子
                    if grid[i][j] == 1:
                        if up == 0 and left == 0:
                            dp[nxt][s] += cnt
                        continue
                    
                    # 尝试两种颜色
                    for color in [1, 2]:
                        if up == color or left == color:
                            continue
                        if grid[i][j] != 0 and grid[i][j] != color:
                            continue
                        
                        ns = set_val(s, j, color)
                        if j > 0:
                            ns = set_val(ns, j-1, 0)
                        dp[nxt][ns] += cnt
                
                cur, nxt = nxt, cur
            
            # 换行处理
            dp[nxt].clear()
            for s, cnt in dp[cur].items():
                valid = True
                for j in range(m):
                    if get(s, j) != 0:
                        valid = False
                        break
                if valid:
                    dp[nxt][s] += cnt
            
            cur, nxt = nxt, cur
        
        print(sum(dp[cur].values()))

if __name__ == "__main__":
    main()

Java 实现代码：
import java.util.*;

public class Main {
    static long[] power3;
    static int n, m;
    static int[][] grid;
    
    static void initPower3() {
        power3 = new long[13];
        power3[0] = 1;
        for (int i = 1; i <= 12; i++) {
            power3[i] = power3[i-1] * 3;
        }
    }
    
    static int get(long s, int pos) {
        return (int) (s / power3[pos] % 3);
    }
    
    static long set(long s, int pos, int val) {
        return s - get(s, pos) * power3[pos] + (long)val * power3[pos];
    }
    
    static long solve() {
        Map<Long, Long>[] dp = new HashMap[2];
        dp[0] = new HashMap<>();
        dp[1] = new HashMap<>();
        int cur = 0, next = 1;
        dp[cur].put(0L, 1L);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[next].clear();
                
                for (Map.Entry<Long, Long> entry : dp[cur].entrySet()) {
                    long s = entry.getKey();
                    long cnt = entry.getValue();
                    
                    int up = get(s, j);
                    int left = j > 0 ? get(s, j-1) : 0;
                    
                    // 障碍格子
                    if (grid[i][j] == 1) {
                        if (up == 0 && left == 0) {
                            dp[next].put(s, dp[next].getOrDefault(s, 0L) + cnt);
                        }
                        continue;
                    }
                    
                    // 尝试两种颜色
                    for (int color = 1; color <= 2; color++) {
                        if (up == color || left == color) {
                            continue;
                        }
                        if (grid[i][j] != 0 && grid[i][j] != color) {
                            continue;
                        }
                        
                        long ns = set(s, j, color);
                        if (j > 0) {
                            ns = set(ns, j-1, 0);
                        }
                        dp[next].put(ns, dp[next].getOrDefault(ns, 0L) + cnt);
                    }
                }
                
                cur = next;
                next = 1 - cur;
            }
            
            // 换行处理
            dp[next].clear();
            for (Map.Entry<Long, Long> entry : dp[cur].entrySet()) {
                long s = entry.getKey();
                long cnt = entry.getValue();
                boolean valid = true;
                for (int j = 0; j < m; j++) {
                    if (get(s, j) != 0) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    dp[next].put(s, dp[next].getOrDefault(s, 0L) + cnt);
                }
            }
            
            cur = next;
            next = 1 - cur;
        }
        
        long ans = 0;
        for (long cnt : dp[cur].values()) {
            ans += cnt;
        }
        return ans;
    }
    
    public static void main(String[] args) {
        initPower3();
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        while (T-- > 0) {
            n = sc.nextInt();
            m = sc.nextInt();
            grid = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }
            System.out.println(solve());
        }
        sc.close();
    }
}

时间复杂度：O(n * m * 3^m)
空间复杂度：O(3^m)

// 插头DP与轮廓线DP总结
/*
插头DP和轮廓线DP是解决网格类问题的高效算法。以下是关键要点：

1. 核心概念：
   - 轮廓线：正在处理的格子的边界
   - 插头：表示轮廓线上的连接状态
   - 状态压缩：使用不同进制表示轮廓线状态

2. 常见状态表示：
   - 二进制：适用于简单的存在性问题
   - 三进制：适用于需要区分插头类型的问题
   - 四进制：适用于更复杂的情况

3. 典型应用场景：
   - 骨牌覆盖问题
   - 网格着色问题
   - 路径覆盖问题
   - 回路覆盖问题
   - 障碍处理问题

4. 优化技巧：
   - 滚动数组优化空间
   - 使用哈希表存储有效状态
   - 预处理可能的转移状态
   - 交换网格行列以减少状态数
   - 剪枝无效状态

5. 工程化考量：
   - 注意数据类型的范围，防止溢出
   - 合理选择状态表示方式
   - 处理边界条件和特殊输入
   - 代码模块化设计
   - 添加详细注释和测试用例

6. 调试技巧：
   - 打印中间状态进行验证
   - 使用小例子测试
   - 检查状态转移的正确性
   - 验证边界条件

7. 学习建议：
   - 从简单问题入手，如骨牌覆盖
   - 理解轮廓线和插头的概念
   - 掌握不同进制状态表示的应用
   - 多练习不同类型的题目
   - 总结状态转移的规律

通过学习和实践这些算法，可以有效地解决各种复杂的网格类问题，提高算法设计和实现能力。
*/
