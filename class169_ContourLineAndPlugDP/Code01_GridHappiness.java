package class126;

// 插头DP和轮廓线DP专题 - 题目1: 最大化网格幸福感
// 给定四个整数m、n、in、ex，表示m*n的网格，以及in个内向的人，ex个外向的人
// 你来决定网格中应当居住多少人，并为每个人分配一个网格单元，不必让所有人都生活在网格中
// 每个人的幸福感计算如下：
// 内向的人开始时有120幸福感，但每存在一个邻居，他都会失去30幸福感
// 外向的人开始时有40幸福感，但每存在一个邻居，他都会得到20幸福感
// 邻居只包含上、下、左、右四个方向
// 网格幸福感是每个人幸福感的总和，返回最大可能的网格幸福感
// 1 <= m、n <= 5
// 1 <= in、ex <= 6
// 测试链接 : https://leetcode.cn/problems/maximize-grid-happiness/
// 这是一个典型的轮廓线DP问题，使用三进制表示每个位置的状态
//
// 题目大意：
// 给定一个m×n的网格，以及一定数量的内向和外向的人，要求在网格中安排这些人，
// 使得总幸福感最大。每个人的幸福感会受到邻居的影响。
//
// 解题思路：
// 使用轮廓线DP，逐格处理，记录每个位置的状态（空、内向、外向）
// 状态表示：用三进制表示轮廓线状态，0表示空，1表示内向，2表示外向
// 状态转移：考虑当前格子是否放置人，以及放置什么类型的人
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code01_GridHappiness.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code01_GridHappiness.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code01_GridHappiness.py

public class Code01_GridHappiness {

	public static int MAXN = 5;

	public static int MAXM = 5;

	public static int MAXP = 7;

	public static int MAXS = (int) Math.pow(3, MAXM);

	public static int n;

	public static int m;

	public static int maxs;

	// dp[i][j][s][a][b] 表示处理到第i行第j列，轮廓线状态为s，剩余a个内向的人，b个外向的人时的最大幸福感
	public static int[][][][][] dp = new int[MAXN][MAXM][MAXS][MAXP][MAXP];

	/**
	 * 计算最大可能的网格幸福感
	 * 
	 * 算法思路：
	 * 使用轮廓线DP，逐格处理，记录每个位置的状态（空、内向、外向）
	 * 状态表示：用三进制表示轮廓线状态，0表示空，1表示内向，2表示外向
	 * 状态转移：考虑当前格子是否放置人，以及放置什么类型的人
	 * 
	 * 时间复杂度：O(n * m * 3^m * in * ex)，其中n和m是网格大小，in和ex是人的数量
	 * 空间复杂度：O(n * m * 3^m * in * ex)
	 * 
	 * @param rows 网格行数
	 * @param cols 网格列数
	 * @param in 内向的人数
	 * @param ex 外向的人数
	 * @return 最大网格幸福感
	 */
	public static int getMaxGridHappiness(int rows, int cols, int in, int ex) {
		n = Math.max(rows, cols);  // 为了优化，将较大的维度作为行
		m = Math.min(rows, cols);  // 将较小的维度作为列，减少状态数
		maxs = (int) Math.pow(3, m);  // 状态总数
		// 初始化DP数组为-1（未访问）
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int s = 0; s < maxs; s++) {
					for (int a = 0; a <= in; a++) {
						for (int b = 0; b <= ex; b++) {
							dp[i][j][s][a][b] = -1;
						}
					}
				}
			}
		}
		return f(0, 0, 0, in, ex, 1);
	}

	/**
	 * 递归函数，计算最大幸福感
	 * 
	 * @param i 当前行
	 * @param j 当前列
	 * @param s 轮廓线状态
	 * @param a 剩余内向的人数
	 * @param b 剩余外向的人数
	 * @param bit 3^j的值，用于快速获取和设置状态位
	 * @return 最大幸福感
	 */
	public static int f(int i, int j, int s, int a, int b, int bit) {
		// 基本情况：处理完所有行
		if (i == n) {
			return 0;
		}
		// 处理完当前行，转移到下一行
		if (j == m) {
			return f(i + 1, 0, s, a, b, 1);
		}
		// 记忆化搜索，如果已经计算过当前状态，直接返回
		if (dp[i][j][s][a][b] != -1) {
			return dp[i][j][s][a][b];
		}
		
		// 情况1：当前格子不安置人
		int ans = f(i, j + 1, set(s, bit, 0), a, b, bit * 3);
		
		// 获取邻居信息
		int up = get(s, bit);      // 上方格子的状态
		int left = j == 0 ? 0 : get(s, bit / 3);  // 左方格子的状态
		int neighbor = 0;  // 邻居数量
		int pre = 0;       // 邻居带来的幸福感变化
		
		// 计算上方邻居带来的影响
		if (up != 0) {
			neighbor++;
			pre += up == 1 ? -30 : 20;  // 内向邻居减30，外向邻居加20
		}
		
		// 计算左方邻居带来的影响
		if (left != 0) {
			neighbor++;
			pre += left == 1 ? -30 : 20;  // 内向邻居减30，外向邻居加20
		}
		
		// 情况2：放置内向的人（如果还有剩余）
		if (a > 0) {
			// 内向的人初始120幸福感，每个邻居减30
			int current = 120 - neighbor * 30;
			int newState = set(s, bit, 1);  // 更新状态，当前位置设置为1（内向）
			ans = Math.max(ans, pre + current + f(i, j + 1, newState, a - 1, b, bit * 3));
		}
		
		// 情况3：放置外向的人（如果还有剩余）
		if (b > 0) {
			// 外向的人初始40幸福感，每个邻居加20
			int current = 40 + neighbor * 20;
			int newState = set(s, bit, 2);  // 更新状态，当前位置设置为2（外向）
			ans = Math.max(ans, pre + current + f(i, j + 1, newState, a, b - 1, bit * 3));
		}
		
		// 保存结果并返回
		dp[i][j][s][a][b] = ans;
		return ans;
	}

	/**
	 * 从状态s中获取第j个位置的值（三进制）
	 * 
	 * @param s 状态值
	 * @param bit 3^j的值
	 * @return 位置j的值（0、1或2）
	 */
	public static int get(int s, int bit) {
		return s / bit % 3;
	}

	/**
	 * 将状态s中第j个位置的值设置为v
	 * 
	 * @param s 原始状态值
	 * @param bit 3^j的值
	 * @param v 新的值（0、1或2）
	 * @return 更新后的状态值
	 */
	public static int set(int s, int bit, int v) {
		return s - get(s, bit) * bit + v * bit;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1：3x3网格，1个内向，1个外向
		System.out.println(getMaxGridHappiness(3, 3, 1, 1));  // 预期输出：260
		
		// 测试用例2：2x2网格，1个内向，2个外向
		System.out.println(getMaxGridHappiness(2, 2, 1, 2));  // 预期输出：240
	}
}

// 补充题目1：LeetCode 790 Domino and Tromino Tiling
/*
题目描述：
有两种形状的瓷砖：一种是 2x1 的多米诺形，另一种是形如 "L" 的托米诺形。两种形状都可以旋转。
给定一个整数 n，返回可以平铺 2x n 的面板的方法的数量。返回对 10^9 + 7 取模的值。

链接：https://leetcode.cn/problems/domino-and-tromino-tiling/

算法解析：
这是一个经典的动态规划问题，可以用插头DP思想来解决。我们定义四种状态：
1. dp[i][0]: 处理到第i列时，第i列没有任何方块
2. dp[i][1]: 处理到第i列时，第i列只有上方有一个方块
3. dp[i][2]: 处理到第i列时，第i列只有下方有一个方块
4. dp[i][3]: 处理到第i列时，第i列两个方块都有

通过分析状态转移方程，可以得到最优解。

C++ 实现代码：
class Solution {
public:
    int numTilings(int n) {
        const int MOD = 1e9 + 7;
        if (n == 1) return 1;
        vector<vector<long long>> dp(n + 1, vector<long long>(4, 0));
        dp[0][0] = 1; // 空状态
        
        for (int i = 1; i <= n; ++i) {
            // 当前列没有任何方块：只能由上一列两个方块都有的状态转移而来
            dp[i][0] = dp[i-1][3];
            
            // 当前列上面有一个方块：由上一列空状态或上一列下面有一个方块转移而来
            dp[i][1] = (dp[i-1][0] + dp[i-1][2]) % MOD;
            
            // 当前列下面有一个方块：由上一列空状态或上一列上面有一个方块转移而来
            dp[i][2] = (dp[i-1][0] + dp[i-1][1]) % MOD;
            
            // 当前列两个方块都有：由上一列所有状态转移而来
            dp[i][3] = (dp[i-1][0] + dp[i-1][1] + dp[i-1][2] + dp[i-1][3]) % MOD;
        }
        
        return dp[n][3] % MOD;
    }
};

Python 实现代码：
class Solution:
    def numTilings(self, n: int) -> int:
        MOD = 10**9 + 7
        if n == 1:
            return 1
        # dp[i][j] 表示处理到第i列时的状态j
        # j=0: 空状态
        # j=1: 上面有一个方块
        # j=2: 下面有一个方块
        # j=3: 两个方块都有
        dp = [[0] * 4 for _ in range(n + 1)]
        dp[0][0] = 1
        
        for i in range(1, n + 1):
            # 当前列没有任何方块
            dp[i][0] = dp[i-1][3]
            
            # 当前列上面有一个方块
            dp[i][1] = (dp[i-1][0] + dp[i-1][2]) % MOD
            
            # 当前列下面有一个方块
            dp[i][2] = (dp[i-1][0] + dp[i-1][1]) % MOD
            
            # 当前列两个方块都有
            dp[i][3] = (dp[i-1][0] + dp[i-1][1] + dp[i-1][2] + dp[i-1][3]) % MOD
        
        return dp[n][3] % MOD

Java 实现代码：
class Solution {
    public int numTilings(int n) {
        final int MOD = 1_000_000_007;
        if (n == 1) return 1;
        // dp[i][j] 表示处理到第i列时的状态j
        long[][] dp = new long[n + 1][4];
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; ++i) {
            dp[i][0] = dp[i-1][3];
            dp[i][1] = (dp[i-1][0] + dp[i-1][2]) % MOD;
            dp[i][2] = (dp[i-1][0] + dp[i-1][1]) % MOD;
            dp[i][3] = (dp[i-1][0] + dp[i-1][1] + dp[i-1][2] + dp[i-1][3]) % MOD;
        }
        
        return (int) (dp[n][3] % MOD);
    }
}

时间复杂度：O(n)
空间复杂度：O(n)，可以优化到O(1)

// 补充题目2：POJ 2411 Mondriaan's Dream
/*
题目描述：
给定一个n×m的网格，问用2×1和1×2的多米诺骨牌完全覆盖该网格有多少种不同的方式。

链接：http://poj.org/problem?id=2411

算法解析：
这是一个经典的骨牌覆盖问题，可以用状态压缩动态规划（轮廓线DP）来解决。
我们使用二进制状态表示每一行的骨牌放置情况，0表示未被覆盖，1表示已被覆盖。
对于每一行，我们枚举所有可能的状态，并与上一行的状态进行匹配，检查是否可以放置骨牌。

C++ 实现代码：
#include <iostream>
#include <vector>
using namespace std;

typedef long long ll;

int main() {
    int n, m;
    while (cin >> n >> m && n && m) {
        vector<vector<ll>> dp(n + 1, vector<ll>(1 << m, 0));
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < (1 << m); ++j) {
                for (int k = 0; k < (1 << m); ++k) {
                    if ((j & k) != 0) continue; // 不能有重叠
                    int t = (j | k);
                    bool valid = true;
                    int cnt = 0;
                    for (int p = 0; p < m; ++p) {
                        if ((t >> p) & 1) {
                            cnt = 0;
                        } else {
                            cnt++;
                            if (cnt & 1) {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if (valid) {
                        dp[i][j] += dp[i-1][k];
                    }
                }
            }
        }
        cout << dp[n][0] << endl;
    }
    return 0;
}

Python 实现代码：
def main():
    import sys
    for line in sys.stdin:
        n, m = map(int, line.strip().split())
        if n == 0 and m == 0:
            break
        # 交换n和m，使得m较小，减少状态数
        if n < m:
            n, m = m, n
        
        dp = [[0] * (1 << m) for _ in range(n + 1)]
        dp[0][0] = 1
        
        for i in range(1, n + 1):
            for j in range(1 << m):
                for k in range(1 << m):
                    if (j & k) != 0:
                        continue
                    t = j | k
                    valid = True
                    cnt = 0
                    for p in range(m):
                        if (t >> p) & 1:
                            cnt = 0
                        else:
                            cnt += 1
                            if cnt % 2 != 0:
                                valid = False
                                break
                    if valid:
                        dp[i][j] += dp[i-1][k]
        
        print(dp[n][0])

if __name__ == "__main__":
    main()

Java 实现代码：
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            if (n == 0 && m == 0) break;
            
            if (n < m) {
                int temp = n;
                n = m;
                m = temp;
            }
            
            long[][] dp = new long[n + 1][1 << m];
            dp[0][0] = 1;
            
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < (1 << m); j++) {
                    for (int k = 0; k < (1 << m); k++) {
                        if ((j & k) != 0) continue;
                        int t = j | k;
                        boolean valid = true;
                        int cnt = 0;
                        for (int p = 0; p < m; p++) {
                            if ((t >> p & 1) == 1) {
                                cnt = 0;
                            } else {
                                cnt++;
                                if (cnt % 2 != 0) {
                                    valid = false;
                                    break;
                                }
                            }
                        }
                        if (valid) {
                            dp[i][j] += dp[i-1][k];
                        }
                    }
                }
            }
            System.out.println(dp[n][0]);
        }
        sc.close();
    }
}

时间复杂度：O(n * 2^m * 2^m)
空间复杂度：O(n * 2^m)

// 补充题目3：HDU 1693 Eat the Trees
/*
题目描述：
给定一个n×m的网格，其中某些格子是障碍物。问有多少种方式可以用1×2或2×1的多米诺骨牌覆盖所有非障碍物格子？

链接：http://acm.hdu.edu.cn/showproblem.php?pid=1693

算法解析：
这是一个带障碍的骨牌覆盖问题，可以用插头DP来解决。我们使用二进制状态表示轮廓线上的插头情况，
对于每个位置，我们根据是否有障碍物以及左右插头的情况，进行状态转移。

C++ 实现代码：
#include <iostream>
#include <cstring>
#include <vector>
using namespace std;

typedef long long ll;
const int MAXN = 15;
const int MAXM = 15;

int n, m;
int grid[MAXN][MAXM];
vector<vector<ll>> dp[MAXN];

void init() {
    for (int i = 0; i < MAXN; ++i) {
        dp[i].resize(MAXM, vector<ll>(1 << MAXM, 0));
    }
}

int main() {
    int T, cas = 1;
    cin >> T;
    while (T--) {
        init();
        cin >> n >> m;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                cin >> grid[i][j];
            }
        }
        
        dp[0][0][0] = 1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                int nexti = i, nextj = j + 1;
                if (nextj == m) {
                    nexti++;
                    nextj = 0;
                }
                
                for (int s = 0; s < (1 << m); ++s) {
                    if (dp[i][j][s] == 0) continue;
                    
                    // 如果当前格子是障碍物
                    if (grid[i][j] == 0) {
                        // 必须没有插头
                        if ((s & (1 << j)) == 0 && (s & (1 << (j+1))) == 0) {
                            dp[nexti][nextj][s] += dp[i][j][s];
                        }
                        continue;
                    }
                    
                    int left = (s >> j) & 1;
                    int up = (s >> (j+1)) & 1;
                    
                    if (left == 0 && up == 0) {
                        // 放置向右和向下的插头
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            int ns = s | (1 << j);
                            dp[nexti][nextj][ns] += dp[i][j][s];
                        }
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            int ns = s | (1 << (j+1));
                            dp[nexti][nextj][ns] += dp[i][j][s];
                        }
                    } else if (left == 1 && up == 0) {
                        // 消除左插头
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            int ns = s & ~(1 << j);
                            dp[nexti][nextj][ns] += dp[i][j][s];
                        }
                        // 左转下
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            int ns = (s & ~(1 << j)) | (1 << (j+1));
                            dp[nexti][nextj][ns] += dp[i][j][s];
                        }
                    } else if (left == 0 && up == 1) {
                        // 上转右
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            int ns = (s & ~(1 << (j+1))) | (1 << j);
                            dp[nexti][nextj][ns] += dp[i][j][s];
                        }
                        // 消除上插头
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            int ns = s & ~(1 << (j+1));
                            dp[nexti][nextj][ns] += dp[i][j][s];
                        }
                    } else {
                        // 同时消除左和上插头
                        int ns = s & ~(1 << j) & ~(1 << (j+1));
                        dp[nexti][nextj][ns] += dp[i][j][s];
                    }
                }
            }
        }
        
        cout << "Case " << cas++ << ": There are " << dp[n][0][0] << " ways to eat the trees." << endl;
    }
    return 0;
}

Python 实现代码：
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    T = int(input[ptr])
    ptr += 1
    cas = 1
    
    for _ in range(T):
        n = int(input[ptr])
        m = int(input[ptr+1])
        ptr += 2
        grid = []
        for _ in range(n):
            row = list(map(int, input[ptr:ptr+m]))
            ptr += m
            grid.append(row)
        
        # 初始化dp数组
        dp = [[[0]*(1<<(m+1)) for _ in range(m)] for __ in range(n)]
        dp[0][0][0] = 1
        
        for i in range(n):
            for j in range(m):
                # 计算下一个位置
                nexti = i
                nextj = j + 1
                if nextj == m:
                    nexti += 1
                    nextj = 0
                
                for s in range(1 << (m+1)):
                    if dp[i][j][s] == 0:
                        continue
                    
                    # 如果当前格子是障碍物
                    if grid[i][j] == 0:
                        # 必须没有插头
                        if (s & (1 << j)) == 0 and (s & (1 << (j+1))) == 0:
                            if nexti < n:
                                dp[nexti][nextj][s] += dp[i][j][s]
                        continue
                    
                    left = (s >> j) & 1
                    up = (s >> (j+1)) & 1
                    
                    if left == 0 and up == 0:
                        # 放置向右的插头
                        if j < m - 1 and grid[i][j+1] == 1:
                            ns = s | (1 << j)
                            if nexti < n:
                                dp[nexti][nextj][ns] += dp[i][j][s]
                        # 放置向下的插头
                        if i < n - 1 and grid[i+1][j] == 1:
                            ns = s | (1 << (j+1))
                            if nexti < n:
                                dp[nexti][nextj][ns] += dp[i][j][s]
                    elif left == 1 and up == 0:
                        # 消除左插头，向右延伸
                        if j < m - 1 and grid[i][j+1] == 1:
                            ns = s & ~(1 << j)
                            if nexti < n:
                                dp[nexti][nextj][ns] += dp[i][j][s]
                        # 左插头转向下
                        if i < n - 1 and grid[i+1][j] == 1:
                            ns = (s & ~(1 << j)) | (1 << (j+1))
                            if nexti < n:
                                dp[nexti][nextj][ns] += dp[i][j][s]
                    elif left == 0 and up == 1:
                        # 上插头转向右
                        if j < m - 1 and grid[i][j+1] == 1:
                            ns = (s & ~(1 << (j+1))) | (1 << j)
                            if nexti < n:
                                dp[nexti][nextj][ns] += dp[i][j][s]
                        # 消除上插头，向下延伸
                        if i < n - 1 and grid[i+1][j] == 1:
                            ns = s & ~(1 << (j+1))
                            if nexti < n:
                                dp[nexti][nextj][ns] += dp[i][j][s]
                    else:
                        # 同时消除左和上插头
                        ns = s & ~(1 << j) & ~(1 << (j+1))
                        if nexti < n:
                            dp[nexti][nextj][ns] += dp[i][j][s]
        
        print(f"Case {cas}: There are {dp[n-1][m-1][0]} ways to eat the trees.")
        cas += 1

if __name__ == "__main__":
    main()

Java 实现代码：
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        int cas = 1;
        while (T-- > 0) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int[][] grid = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }
            
            long[][][] dp = new long[n][m][1 << (m + 1)];
            dp[0][0][0] = 1;
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int nexti = i;
                    int nextj = j + 1;
                    if (nextj == m) {
                        nexti++;
                        nextj = 0;
                    }
                    
                    for (int s = 0; s < (1 << (m + 1)); s++) {
                        if (dp[i][j][s] == 0) continue;
                        
                        // 当前格子是障碍物
                        if (grid[i][j] == 0) {
                            if ((s & (1 << j)) == 0 && (s & (1 << (j + 1))) == 0) {
                                if (nexti < n) {
                                    dp[nexti][nextj][s] += dp[i][j][s];
                                }
                            }
                            continue;
                        }
                        
                        int left = (s >> j) & 1;
                        int up = (s >> (j + 1)) & 1;
                        
                        if (left == 0 && up == 0) {
                            // 放置向右的插头
                            if (j < m - 1 && grid[i][j + 1] == 1) {
                                int ns = s | (1 << j);
                                if (nexti < n) {
                                    dp[nexti][nextj][ns] += dp[i][j][s];
                                }
                            }
                            // 放置向下的插头
                            if (i < n - 1 && grid[i + 1][j] == 1) {
                                int ns = s | (1 << (j + 1));
                                if (nexti < n) {
                                    dp[nexti][nextj][ns] += dp[i][j][s];
                                }
                            }
                        } else if (left == 1 && up == 0) {
                            // 向右延伸
                            if (j < m - 1 && grid[i][j + 1] == 1) {
                                int ns = s & ~(1 << j);
                                if (nexti < n) {
                                    dp[nexti][nextj][ns] += dp[i][j][s];
                                }
                            }
                            // 向下延伸
                            if (i < n - 1 && grid[i + 1][j] == 1) {
                                int ns = (s & ~(1 << j)) | (1 << (j + 1));
                                if (nexti < n) {
                                    dp[nexti][nextj][ns] += dp[i][j][s];
                                }
                            }
                        } else if (left == 0 && up == 1) {
                            // 向右延伸
                            if (j < m - 1 && grid[i][j + 1] == 1) {
                                int ns = (s & ~(1 << (j + 1))) | (1 << j);
                                if (nexti < n) {
                                    dp[nexti][nextj][ns] += dp[i][j][s];
                                }
                            }
                            // 向下延伸
                            if (i < n - 1 && grid[i + 1][j] == 1) {
                                int ns = s & ~(1 << (j + 1));
                                if (nexti < n) {
                                    dp[nexti][nextj][ns] += dp[i][j][s];
                                }
                            }
                        } else {
                            // 同时消除两个插头
                            int ns = s & ~(1 << j) & ~(1 << (j + 1));
                            if (nexti < n) {
                                dp[nexti][nextj][ns] += dp[i][j][s];
                            }
                        }
                    }
                }
            }
            
            System.out.println("Case " + cas++ + ": There are " + dp[n - 1][m - 1][0] + " ways to eat the trees.");
        }
        sc.close();
    }
}

时间复杂度：O(n * m * 2^(m+1))
空间复杂度：O(n * m * 2^(m+1))

// 补充题目4：UVA 10572 Black and White
/*
题目描述：
给定一个n×m的网格，每个格子可以是黑色、白色或未着色。要求给所有未着色的格子着色，使得相邻的格子颜色不同。
此外，必须恰好有B个黑色格子和W个白色格子。求有多少种不同的着色方式？

链接：https://vjudge.net/problem/UVA-10572

算法解析：
这是一个带约束的网格着色问题，可以用插头DP来解决。我们使用三进制状态表示轮廓线上的颜色情况，
0表示未着色，1表示黑色，2表示白色。同时需要记录已经使用的黑色和白色格子数量。

C++ 实现代码：
#include <iostream>
#include <cstring>
#include <vector>
using namespace std;

typedef long long ll;
const int MAXN = 12;
const int MAXM = 12;
const int MAXB = 65;
const int MAXW = 65;
const int MAXS = 531441; // 3^12

int n, m, B, W;
int grid[MAXN][MAXM];
ll dp[MAXN][MAXM][MAXS][2][2]; // 优化：只记录连通性信息

int power3[MAXM];

void initPower3() {
    power3[0] = 1;
    for (int i = 1; i < MAXM; ++i) {
        power3[i] = power3[i-1] * 3;
    }
}

int get(int s, int pos) {
    return s / power3[pos] % 3;
}

int set(int s, int pos, int val) {
    return s - get(s, pos) * power3[pos] + val * power3[pos];
}

ll solve() {
    memset(dp, 0, sizeof(dp));
    dp[0][0][0][0][0] = 1;
    
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            for (int s = 0; s < power3[m]; ++s) {
                for (int bc = 0; bc < 2; ++bc) {
                    for (int wc = 0; wc < 2; ++wc) {
                        if (dp[i][j][s][bc][wc] == 0) continue;
                        
                        int nexti = i, nextj = j + 1;
                        if (nextj == m) {
                            nexti++;
                            nextj = 0;
                        }
                        
                        int up = get(s, j);
                        int left = j > 0 ? get(s, j-1) : 0;
                        
                        if (grid[i][j] == 1) {
                            // 必须是黑色
                            if (up == 2 || left == 2) continue;
                            int newbc = bc || (up == 0 && left == 0);
                            int newwc = wc;
                            int ns = set(s, j, 1);
                            dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                        } else if (grid[i][j] == 2) {
                            // 必须是白色
                            if (up == 1 || left == 1) continue;
                            int newbc = bc;
                            int newwc = wc || (up == 0 && left == 0);
                            int ns = set(s, j, 2);
                            dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                        } else {
                            // 可以选择黑色
                            if (up != 2 && left != 2) {
                                int newbc = bc || (up == 0 && left == 0);
                                int newwc = wc;
                                int ns = set(s, j, 1);
                                dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                            }
                            // 可以选择白色
                            if (up != 1 && left != 1) {
                                int newbc = bc;
                                int newwc = wc || (up == 0 && left == 0);
                                int ns = set(s, j, 2);
                                dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                            }
                        }
                    }
                }
            }
        }
    }
    
    ll res = 0;
    for (int s = 0; s < power3[m]; ++s) {
        res += dp[n][0][s][1][1];
    }
    return res;
}

int main() {
    initPower3();
    int T;
    cin >> T;
    while (T--) {
        cin >> n >> m >> B >> W;
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
    sys.setrecursionlimit(1 << 25)
    
    # 预计算3的幂次
    power3 = [1] * 12
    for i in range(1, 12):
        power3[i] = power3[i-1] * 3
    
    def get(s, pos):
        return s // power3[pos] % 3
    
    def set_val(s, pos, val):
        return s - get(s, pos) * power3[pos] + val * power3[pos]
    
    T = int(sys.stdin.readline())
    for _ in range(T):
        n, m, B, W = map(int, sys.stdin.readline().split())
        grid = []
        for _ in range(n):
            row = list(map(int, sys.stdin.readline().split()))
            grid.append(row)
        
        # 初始化dp数组
        dp = [[[[[0]*2 for _ in range(2)] for __ in range(power3[m])] for ___ in range(m)] for ____ in range(n+1)]
        dp[0][0][0][0][0] = 1
        
        for i in range(n):
            for j in range(m):
                for s in range(power3[m]):
                    for bc in range(2):
                        for wc in range(2):
                            if dp[i][j][s][bc][wc] == 0:
                                continue
                            
                            nexti = i
                            nextj = j + 1
                            if nextj == m:
                                nexti += 1
                                nextj = 0
                            
                            up = get(s, j)
                            left = get(s, j-1) if j > 0 else 0
                            
                            if grid[i][j] == 1:
                                # 必须是黑色
                                if up != 2 and left != 2:
                                    newbc = bc or (up == 0 and left == 0)
                                    newwc = wc
                                    ns = set_val(s, j, 1)
                                    dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc]
                            elif grid[i][j] == 2:
                                # 必须是白色
                                if up != 1 and left != 1:
                                    newbc = bc
                                    newwc = wc or (up == 0 and left == 0)
                                    ns = set_val(s, j, 2)
                                    dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc]
                            else:
                                # 可以选择黑色
                                if up != 2 and left != 2:
                                    newbc = bc or (up == 0 and left == 0)
                                    newwc = wc
                                    ns = set_val(s, j, 1)
                                    dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc]
                                # 可以选择白色
                                if up != 1 and left != 1:
                                    newbc = bc
                                    newwc = wc or (up == 0 and left == 0)
                                    ns = set_val(s, j, 2)
                                    dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc]
        
        res = 0
        for s in range(power3[m]):
            res += dp[n][0][s][1][1]
        print(res)

if __name__ == "__main__":
    main()

Java 实现代码：
import java.util.*;

public class Main {
    static final int MAXN = 12;
    static final int MAXM = 12;
    static long[][][][][] dp = new long[MAXN + 1][MAXM][531441][2][2]; // 3^12 = 531441
    static int[] power3 = new int[MAXM];
    
    static void initPower3() {
        power3[0] = 1;
        for (int i = 1; i < MAXM; i++) {
            power3[i] = power3[i-1] * 3;
        }
    }
    
    static int get(int s, int pos) {
        return s / power3[pos] % 3;
    }
    
    static int set(int s, int pos, int val) {
        return s - get(s, pos) * power3[pos] + val * power3[pos];
    }
    
    public static void main(String[] args) {
        initPower3();
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        while (T-- > 0) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int B = sc.nextInt();
            int W = sc.nextInt();
            int[][] grid = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }
            
            // 重置dp数组
            for (int i = 0; i <= n; i++) {
                for (int j = 0; j < m; j++) {
                    for (int s = 0; s < power3[m]; s++) {
                        for (int bc = 0; bc < 2; bc++) {
                            for (int wc = 0; wc < 2; wc++) {
                                dp[i][j][s][bc][wc] = 0;
                            }
                        }
                    }
                }
            }
            
            dp[0][0][0][0][0] = 1;
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    for (int s = 0; s < power3[m]; s++) {
                        for (int bc = 0; bc < 2; bc++) {
                            for (int wc = 0; wc < 2; wc++) {
                                if (dp[i][j][s][bc][wc] == 0) continue;
                                
                                int nexti = i;
                                int nextj = j + 1;
                                if (nextj == m) {
                                    nexti++;
                                    nextj = 0;
                                }
                                
                                int up = get(s, j);
                                int left = j > 0 ? get(s, j-1) : 0;
                                
                                if (grid[i][j] == 1) {
                                    // 必须是黑色
                                    if (up != 2 && left != 2) {
                                        int newbc = bc | (up == 0 && left == 0 ? 1 : 0);
                                        int newwc = wc;
                                        int ns = set(s, j, 1);
                                        dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                                    }
                                } else if (grid[i][j] == 2) {
                                    // 必须是白色
                                    if (up != 1 && left != 1) {
                                        int newbc = bc;
                                        int newwc = wc | (up == 0 && left == 0 ? 1 : 0);
                                        int ns = set(s, j, 2);
                                        dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                                    }
                                } else {
                                    // 可以选择黑色
                                    if (up != 2 && left != 2) {
                                        int newbc = bc | (up == 0 && left == 0 ? 1 : 0);
                                        int newwc = wc;
                                        int ns = set(s, j, 1);
                                        dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                                    }
                                    // 可以选择白色
                                    if (up != 1 && left != 1) {
                                        int newbc = bc;
                                        int newwc = wc | (up == 0 && left == 0 ? 1 : 0);
                                        int ns = set(s, j, 2);
                                        dp[nexti][nextj][ns][newbc][newwc] += dp[i][j][s][bc][wc];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            long res = 0;
            for (int s = 0; s < power3[m]; s++) {
                res += dp[n][0][s][1][1];
            }
            System.out.println(res);
        }
        sc.close();
    }
}

时间复杂度：O(n * m * 3^m * 2 * 2)
空间复杂度：O(n * m * 3^m * 2 * 2)

        dp = [0] * (n + 1)
        dp[0] = 1
        dp[1] = 1
        dp[2] = 2
        for i in range(3, n + 1):
            dp[i] = (2 * dp[i-1] + dp[i-3]) % MOD
        return dp[n]

Java 实现代码：
class Solution {
    public int numTilings(int n) {
        final int MOD = 1_000_000_007;
        if (n == 1) return 1;
        long[] dp = new long[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = (2 * dp[i-1] + dp[i-3]) % MOD;
        }
        return (int) dp[n];
    }
}

时间复杂度：O(n)
空间复杂度：O(n)，可以优化到O(1)只使用几个变量
*/

// 补充题目2：洛谷P5056 【模板】插头DP
/*
题目描述：
给出一个 n×m 的方格，有些格子不能铺线，其它格子必须铺，形成一个闭合回路。问有多少种铺法？

链接：https://www.luogu.com.cn/problem/P5056

C++ 实现代码：
#include <iostream>
#include <cstring>
#include <unordered_map>
using namespace std;

typedef long long ll;
const int MAXN = 12;
const int MAXM = 12;

ll n, m;
ll grid[MAXN][MAXM];
ll endx, endy;
ll cnt;

// 使用哈希表优化空间
unordered_map<ll, ll> dp[2];
ll cur;

// 获取状态s中第j位的值
ll get(ll s, ll j) {
    return (s >> (j * 2)) & 3;
}

// 设置状态s中第j位的值为v
ll set(ll s, ll j, ll v) {
    return s ^ ((ll)get(s, j) ^ v) << (j * 2);
}

// 找到与当前左括号匹配的右括号位置
ll find(ll s, ll j) {
    ll cnt = 1;
    for (ll i = j + 1; i <= m; i++) {
        if (get(s, i) == 1) cnt++;
        if (get(s, i) == 2) cnt--;
        if (cnt == 0) return i;
    }
    return -1;
}

// 最小表示法优化状态
ll min_rep(ll s) {
    ll a[MAXM + 1], b[MAXM + 1];
    memset(b, 0, sizeof(b));
    ll cnt = 0;
    for (ll i = 0; i <= m; i++) {
        a[i] = get(s, i);
        if (a[i] != 0 && b[a[i]] == 0) {
            b[a[i]] = ++cnt;
        }
        if (a[i] != 0) a[i] = b[a[i]];
    }
    ll res = 0;
    for (ll i = 0; i <= m; i++) {
        res = res * 3 + a[i];
    }
    return res;
}

void solve() {
    cnt = 0;
    for (ll i = 1; i <= n; i++) {
        for (ll j = 1; j <= m; j++) {
            cin >> grid[i][j];
            if (grid[i][j]) {
                cnt++;
                endx = i;
                endy = j;
            }
        }
    }
    
    if (cnt == 0) {
        cout << 0 << endl;
        return;
    }
    
    cur = 0;
    dp[cur].clear();
    dp[cur][0] = 1;
    
    for (ll i = 1; i <= n; i++) {
        // 状态转移到下一行，左移一位
        unordered_map<ll, ll> temp;
        for (auto& p : dp[cur]) {
            ll s = p.first;
            ll num = p.second;
            if (get(s, m) == 0) {
                temp[s << 2] += num;
            }
        }
        dp[cur] = temp;
        
        for (ll j = 1; j <= m; j++) {
            dp[cur ^ 1].clear();
            for (auto& p : dp[cur]) {
                ll s = p.first;
                ll num = p.second;
                
                ll left = get(s, j - 1);
                ll up = get(s, j);
                
                if (!grid[i][j]) {
                    if (left == 0 && up == 0) {
                        dp[cur ^ 1][s] += num;
                    }
                    continue;
                }
                
                if (left == 0 && up == 0) {
                    if (i < n && j < m) {
                        // 放置一个新的插头对
                        ll ns = set(s, j - 1, 1);
                        ns = set(ns, j, 2);
                        dp[cur ^ 1][ns] += num;
                    }
                } else if (left == 0 && up != 0) {
                    // 向右延伸
                    if (j < m) {
                        ll ns = set(s, j - 1, up);
                        ns = set(ns, j, 0);
                        dp[cur ^ 1][ns] += num;
                    }
                    // 向下延伸
                    if (i < n) {
                        dp[cur ^ 1][s] += num;
                    }
                } else if (left != 0 && up == 0) {
                    // 向右延伸
                    if (j < m) {
                        dp[cur ^ 1][s] += num;
                    }
                    // 向下延伸
                    if (i < n) {
                        ll ns = set(s, j - 1, 0);
                        ns = set(ns, j, left);
                        dp[cur ^ 1][ns] += num;
                    }
                } else if (left == 1 && up == 1) {
                    // 合并两个左括号，需要找到右括号并修改
                    ll k = find(s, j);
                    ll ns = set(s, j - 1, 0);
                    ns = set(ns, j, 0);
                    ns = set(ns, k, 1);
                    dp[cur ^ 1][ns] += num;
                } else if (left == 2 && up == 2) {
                    // 合并两个右括号，需要找到左括号并修改
                    ll k = find(s, j - 1);
                    ll ns = set(s, j - 1, 0);
                    ns = set(ns, j, 0);
                    ns = set(ns, k, 2);
                    dp[cur ^ 1][ns] += num;
                } else if (left == 2 && up == 1) {
                    // 合并一个右括号和一个左括号
                    ll ns = set(s, j - 1, 0);
                    ns = set(ns, j, 0);
                    // 如果是最后一个格子，检查是否形成闭合回路
                    if (i == endx && j == endy && ns == 0) {
                        dp[cur ^ 1][ns] += num;
                    } else if (ns != 0) {
                        dp[cur ^ 1][ns] += num;
                    }
                } else if (left == 1 && up == 2) {
                    // 形成闭合回路
                    ll ns = set(s, j - 1, 0);
                    ns = set(ns, j, 0);
                    if (i == endx && j == endy && ns == 0) {
                        dp[cur ^ 1][ns] += num;
                    }
                }
            }
            cur ^= 1;
        }
    }
    
    cout << dp[cur][0] << endl;
}

int main() {
    solve();
    return 0;
}

Python 实现代码：
import sys
from collections import defaultdict

MOD = 10**9 + 7

class Solution:
    def main(self):
        n, m = map(int, sys.stdin.readline().split())
        grid = []
        endx, endy = 0, 0
        cnt = 0
        for i in range(n):
            row = list(map(int, sys.stdin.readline().split()))
            grid.append(row)
            for j in range(m):
                if row[j]:
                    cnt += 1
                    endx, endy = i, j
        
        if cnt == 0:
            print(0)
            return
        
        dp = [defaultdict(int), defaultdict(int)]
        cur = 0
        dp[cur][0] = 1
        
        for i in range(n):
            # 状态转移到下一行，左移一位
            new_dp = defaultdict(int)
            for s, num in dp[cur].items():
                if (s >> (m * 2)) & 3 == 0:
                    new_dp[s << 2] += num
            dp[cur] = new_dp
            
            for j in range(m):
                dp[cur ^ 1].clear()
                for s, num in dp[cur].items():
                    left = (s >> ((j - 1) * 2)) & 3 if j > 0 else 0
                    up = (s >> (j * 2)) & 3
                    
                    if not grid[i][j]:
                        if left == 0 and up == 0:
                            dp[cur ^ 1][s] += num
                        continue
                    
                    if left == 0 and up == 0:
                        if i < n - 1 and j < m - 1:
                            ns = s
                            ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                            ns |= 1 << ((j - 1) * 2) if j > 0 else ns
                            ns &= ~(3 << (j * 2))
                            ns |= 2 << (j * 2)
                            dp[cur ^ 1][ns] += num
                    elif left == 0 and up != 0:
                        if j < m - 1:
                            ns = s
                            ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                            ns |= up << ((j - 1) * 2) if j > 0 else ns
                            ns &= ~(3 << (j * 2))
                            dp[cur ^ 1][ns] += num
                        if i < n - 1:
                            dp[cur ^ 1][s] += num
                    elif left != 0 and up == 0:
                        if j < m - 1:
                            dp[cur ^ 1][s] += num
                        if i < n - 1:
                            ns = s
                            ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                            ns &= ~(3 << (j * 2))
                            ns |= left << (j * 2)
                            dp[cur ^ 1][ns] += num
                    elif left == 1 and up == 1:
                        k = self.find(s, j, m)
                        ns = s
                        ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                        ns &= ~(3 << (j * 2))
                        ns &= ~(3 << (k * 2))
                        ns |= 1 << (k * 2)
                        dp[cur ^ 1][ns] += num
                    elif left == 2 and up == 2:
                        k = self.find_left(s, j - 1, m)
                        ns = s
                        ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                        ns &= ~(3 << (j * 2))
                        ns &= ~(3 << (k * 2))
                        ns |= 2 << (k * 2)
                        dp[cur ^ 1][ns] += num
                    elif left == 2 and up == 1:
                        ns = s
                        ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                        ns &= ~(3 << (j * 2))
                        if (i == endx and j == endy) or ns != 0:
                            dp[cur ^ 1][ns] += num
                    elif left == 1 and up == 2:
                        ns = s
                        ns &= ~(3 << ((j - 1) * 2)) if j > 0 else ns
                        ns &= ~(3 << (j * 2))
                        if i == endx and j == endy and ns == 0:
                            dp[cur ^ 1][ns] += num
                
                cur ^= 1
        
        print(dp[cur].get(0, 0))
    
    def find(self, s, j, m):
        cnt = 1
        for i in range(j + 1, m + 1):
            val = (s >> (i * 2)) & 3
            if val == 1:
                cnt += 1
            elif val == 2:
                cnt -= 1
                if cnt == 0:
                    return i
        return -1
    
    def find_left(self, s, j, m):
        cnt = 1
        for i in range(j - 1, -1, -1):
            val = (s >> (i * 2)) & 3
            if val == 2:
                cnt += 1
            elif val == 1:
                cnt -= 1
                if cnt == 0:
                    return i
        return -1

if __name__ == "__main__":
    solution = Solution()
    solution.main()

Java 实现代码：
import java.util.*;

public class Main {
    static final int MOD = 1000000007;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[][] grid = new int[n][m];
        int endx = 0, endy = 0;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = sc.nextInt();
                if (grid[i][j] == 1) {
                    cnt++;
                    endx = i;
                    endy = j;
                }
            }
        }
        sc.close();
        
        if (cnt == 0) {
            System.out.println(0);
            return;
        }
        
        Map<Long, Long>[] dp = new Map[2];
        dp[0] = new HashMap<>();
        dp[1] = new HashMap<>();
        int cur = 0;
        dp[cur].put(0L, 1L);
        
        for (int i = 0; i < n; i++) {
            // 状态转移到下一行，左移一位
            Map<Long, Long> newDp = new HashMap<>();
            for (Map.Entry<Long, Long> entry : dp[cur].entrySet()) {
                long s = entry.getKey();
                long num = entry.getValue();
                if (get(s, m) == 0) {
                    long newS = s << 2;
                    newDp.put(newS, (newDp.getOrDefault(newS, 0L) + num) % MOD);
                }
            }
            dp[cur] = newDp;
            
            for (int j = 0; j < m; j++) {
                dp[cur ^ 1].clear();
                for (Map.Entry<Long, Long> entry : dp[cur].entrySet()) {
                    long s = entry.getKey();
                    long num = entry.getValue();
                    
                    int left = j > 0 ? get(s, j - 1) : 0;
                    int up = get(s, j);
                    
                    if (grid[i][j] == 0) {
                        if (left == 0 && up == 0) {
                            dp[cur ^ 1].put(s, (dp[cur ^ 1].getOrDefault(s, 0L) + num) % MOD);
                        }
                        continue;
                    }
                    
                    if (left == 0 && up == 0) {
                        if (i < n - 1 && j < m - 1) {
                            long ns = set(s, j - 1, 1, m);
                            ns = set(ns, j, 2, m);
                            dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                        }
                    } else if (left == 0 && up != 0) {
                        if (j < m - 1) {
                            long ns = set(s, j - 1, up, m);
                            ns = set(ns, j, 0, m);
                            dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                        }
                        if (i < n - 1) {
                            dp[cur ^ 1].put(s, (dp[cur ^ 1].getOrDefault(s, 0L) + num) % MOD);
                        }
                    } else if (left != 0 && up == 0) {
                        if (j < m - 1) {
                            dp[cur ^ 1].put(s, (dp[cur ^ 1].getOrDefault(s, 0L) + num) % MOD);
                        }
                        if (i < n - 1) {
                            long ns = set(s, j - 1, 0, m);
                            ns = set(ns, j, left, m);
                            dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                        }
                    } else if (left == 1 && up == 1) {
                        int k = find(s, j, m);
                        long ns = set(s, j - 1, 0, m);
                        ns = set(ns, j, 0, m);
                        ns = set(ns, k, 1, m);
                        dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                    } else if (left == 2 && up == 2) {
                        int k = findLeft(s, j - 1, m);
                        long ns = set(s, j - 1, 0, m);
                        ns = set(ns, j, 0, m);
                        ns = set(ns, k, 2, m);
                        dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                    } else if (left == 2 && up == 1) {
                        long ns = set(s, j - 1, 0, m);
                        ns = set(ns, j, 0, m);
                        if ((i == endx && j == endy) || ns != 0) {
                            dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                        }
                    } else if (left == 1 && up == 2) {
                        long ns = set(s, j - 1, 0, m);
                        ns = set(ns, j, 0, m);
                        if (i == endx && j == endy && ns == 0) {
                            dp[cur ^ 1].put(ns, (dp[cur ^ 1].getOrDefault(ns, 0L) + num) % MOD);
                        }
                    }
                }
                cur ^= 1;
            }
        }
        
        System.out.println(dp[cur].getOrDefault(0L, 0L) % MOD);
    }
    
    static int get(long s, int j) {
        return (int) ((s >> (j * 2)) & 3);
    }
    
    static long set(long s, int j, int v, int m) {
        if (j < 0 || j > m) return s;
        return s ^ ((long)(get(s, j) ^ v) << (j * 2));
    }
    
    static int find(long s, int j, int m) {
        int cnt = 1;
        for (int i = j + 1; i <= m; i++) {
            int val = get(s, i);
            if (val == 1) cnt++;
            if (val == 2) cnt--;
            if (cnt == 0) return i;
        }
        return -1;
    }
    
    static int findLeft(long s, int j, int m) {
        int cnt = 1;
        for (int i = j - 1; i >= 0; i--) {
            int val = get(s, i);
            if (val == 2) cnt++;
            if (val == 1) cnt--;
            if (cnt == 0) return i;
        }
        return -1;
    }
}

时间复杂度：O(n * m * 状态数)，状态数取决于编码方式，这里使用括号表示法，状态数为O(Catalan(m))
空间复杂度：O(状态数)，通过滚动数组和哈希表优化
*/
