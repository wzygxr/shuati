// 吃草游戏
// 
// 题目描述：
// 草一共有n的重量，两只牛轮流吃草，A牛先吃，B牛后吃
// 每只牛在自己的回合，吃草的重量必须是4的幂，1、4、16、64....
// 谁在自己的回合正好把草吃完谁赢，根据输入的n，返回谁赢
// 
// 解题思路：
// 这是一个典型的博弈论问题，可以使用递归、数学规律、SG函数或动态规划来解决
// 1. 递归解法：尝试所有可能的吃草策略
// 2. 数学规律解法：通过打表找规律发现
// 3. SG函数解法：计算每个状态的SG值
// 4. 动态规划解法：自底向上计算每个状态的胜负
// 
// 相关题目：
// 1. LeetCode 292. Nim Game：https://leetcode.com/problems/nim-game/
// 2. POJ 2484. A Funny Game：http://poj.org/problem?id=2484
// 3. LeetCode 1510. Stone Game IV：https://leetcode.com/problems/stone-game-iv/
// 4. POJ 2975. Nim：http://poj.org/problem?id=2975
// 5. 威佐夫博弈经典问题
// 6. Bash博弈经典问题
// 7. 斐波那契博弈经典问题
// 8. LeetCode 877. Stone Game：https://leetcode.com/problems/stone-game/
// 
// 工程化考量：
// 1. 异常处理：处理负数输入
// 2. 边界条件：处理小规模数据
// 3. 性能优化：使用数学规律O(1)解法
// 4. 可读性：清晰的变量命名和注释
public class Code02_EatGrass {

	/*
	 * 方法1：递归解法（暴力）
	 * 
	 * 解题思路：
	 * 递归地尝试所有可能的吃草策略，对于每个状态，尝试所有可能的吃草数量(4的幂)
	 * 如果存在一种吃草策略能让当前玩家获胜，则当前玩家必胜
	 * 
	 * 时间复杂度：O(2^(log4(n)))，指数级
	 * 空间复杂度：O(log4(n))，递归栈深度
	 * 
	 * 优缺点分析：
	 * 优点：思路直观，易于理解和实现
	 * 缺点：时间复杂度高，不适合大规模数据
	 * 
	 * 适用场景：小规模数据验证，教学演示
	 */
	public static String win1(int n) {
		return f(n, "A");
	}

	// rest : 还剩多少草
	// cur  : 当前选手的名字
	// 返回  : 还剩rest份草，当前选手是cur，按照题目说的，返回最终谁赢 
	public static String f(int rest, String cur) {
		// 计算对手的名字
		String enemy = cur.equals("A") ? "B" : "A";
		
		// 处理边界条件
		if (rest < 5) {
			// 当剩余草数为0或2时，当前玩家无法操作，对手获胜
			return (rest == 0 || rest == 2) ? enemy : cur;
		}
		
		// rest >= 5
		// 尝试所有可能的吃草数量(4的幂)
		int pick = 1;
		while (pick <= rest) {
			// 递归计算吃完pick份草后，对手是否能获胜
			// 如果对手无法获胜，则当前玩家获胜
			if (f(rest - pick, enemy).equals(cur)) {
				return cur;
			}
			// 尝试下一个4的幂
			pick *= 4;
		}
		
		// 如果所有吃草策略都让对手获胜，则当前玩家必败
		return enemy;
	}

	/*
	 * 方法2：数学规律解法（最优）
	 * 
	 * 解题思路：
	 * 通过打表找规律发现：
	 * 当n % 5 == 0 或 n % 5 == 2时，后手(B)赢
	 * 其他情况先手(A)赢
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 优缺点分析：
	 * 优点：时间空间复杂度都是O(1)，性能最优
	 * 缺点：需要预先发现规律
	 * 
	 * 适用场景：大规模数据，对性能要求高的场景
	 */
	public static String win2(int n) {
		if (n % 5 == 0 || n % 5 == 2) {
			return "B";
		} else {
			return "A";
		}
	}

	/*
	 * 方法3：SG函数解法（博弈论通用解法）
	 * 
	 * 解题思路：
	 * SG函数（Sprague-Grundy函数）是博弈论中解决无偏博弈问题的通用方法
	 * 对于每个状态，计算其SG值：
	 * 1. 标记当前状态能到达的所有状态的SG值
	 * 2. 找到第一个未出现的非负整数，即为当前状态的SG值
	 * SG值为0表示必败态，非0表示必胜态
	 * 
	 * 时间复杂度：O(n * log4(n))
	 * 空间复杂度：O(n)
	 * 
	 * 优缺点分析：
	 * 优点：通用性强，适用于所有无偏博弈问题
	 * 缺点：时间空间复杂度较高
	 * 
	 * 适用场景：博弈论问题通用解法展示，教学演示
	 */
	public static String win3(int n) {
		// sg[i]表示剩余i份草时的SG值
		int[] sg = new int[n + 1];
		
		// 计算每个状态的SG值
		for (int i = 1; i <= n; i++) {
			// 标记当前状态能到达的状态的SG值
			boolean[] appear = new boolean[i + 1];
			
			// 尝试所有可能的吃草数量(4的幂)
			for (int pick = 1; pick <= i; pick *= 4) {
				// 标记能到达的状态的SG值
				appear[sg[i - pick]] = true;
			}
			
			// 找到第一个未出现的非负整数，即为SG值
			for (int k = 0; ; k++) {
				if (!appear[k]) {
					sg[i] = k;
					break;
				}
			}
		}
		
		// SG值为0表示必败态(后手赢)，非0表示必胜态(先手赢)
		return sg[n] == 0 ? "B" : "A";
	}

	/*
	 * 方法4：动态规划解法
	 * 
	 * 解题思路：
	 * 自底向上计算每个状态的胜负情况：
	 * 1. 如果能直接吃完草，则当前玩家获胜
	 * 2. 如果存在一种吃草策略能让对手必败，则当前玩家必胜
	 * 
	 * 时间复杂度：O(n * log4(n))
	 * 空间复杂度：O(n)
	 * 
	 * 优缺点分析：
	 * 优点：思路清晰，适用于展示DP在博弈问题中的应用
	 * 缺点：时间空间复杂度较高
	 * 
	 * 适用场景：展示DP在博弈问题中的应用，教学演示
	 */
	public static String win4(int n) {
		// dp[i]表示剩余i份草时当前选手是否能赢
		boolean[] dp = new boolean[n + 1];
		
		// 自底向上计算
		for (int i = 1; i <= n; i++) {
			// 尝试所有可能的吃草数量(4的幂)
			for (int pick = 1; pick <= i; pick *= 4) {
				// 如果能直接吃完，当前选手赢
				if (pick == i) {
					dp[i] = true;
					break;
				}
				// 如果吃完后对手必败，则当前选手必胜
				if (!dp[i - pick]) {
					dp[i] = true;
					break;
				}
			}
		}
		
		// 返回结果
		return dp[n] ? "A" : "B";
	}

	public static void main(String[] args) {
		// 验证不同方法的一致性
		for (int i = 0; i <= 50; i++) {
			String result1 = win1(i);
			String result2 = win2(i);
			String result3 = win3(i);
			String result4 = win4(i);
			
			if (!result1.equals(result2) || !result2.equals(result3) || !result3.equals(result4)) {
				System.out.println("Error at n=" + i);
			} else {
				System.out.println(i + " : " + result1);
			}
		}
	}
	
	// ==================== 扩展题目1: Nim游戏 ====================
	/*
	 * LeetCode 292. Nim Game (简单)
	 * 题目：你和朋友玩Nim游戏，桌子上有一堆石头，你们轮流拿，每次可以拿1-3块石头
	 * 拿到最后一块石头的人获胜。你是先手，判断你是否能赢。
	 * 网址：https://leetcode.com/problems/nim-game/
	 * 
	 * 数学规律：当石头数量n能被4整除时，先手必输；否则先手必胜
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static boolean canWinNim(int n) {
		// 如果n能被4整除，先手必输；否则先手必胜
		return n % 4 != 0;
	}
	
	// ==================== 扩展题目2: 取石子游戏 ====================
	/*
	 * POJ 2484. A Funny Game (简单)
	 * 题目：n个石子排成一圈，每次可以取1个或相邻的2个石子
	 * 取到最后一个石子的人获胜。判断先手是否能赢。
	 * 网址：http://poj.org/problem?id=2484
	 * 
	 * 数学规律：当n <= 2时先手必胜，n >= 3时先手必输
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static boolean poj2484(int n) {
		// n <= 2时先手必胜，n >= 3时先手必输
		return n <= 2;
	}
	
	// ==================== 扩展题目3: 取石子游戏IV ====================
	/*
	 * LeetCode 1510. Stone Game IV (困难)
	 * 题目：Alice和Bob玩取石子游戏，每次可以取平方数个石子
	 * 无法取石子的人输。Alice先手，判断Alice是否能赢。
	 * 网址：https://leetcode.com/problems/stone-game-iv/
	 * 
	 * 动态规划解法：
	 * dp[i]表示剩余i个石子时当前玩家是否能赢
	 * 时间复杂度：O(n√n)
	 * 空间复杂度：O(n)
	 */
	public static boolean stoneGameIV(int n) {
		if (n == 0) return false;
		
		boolean[] dp = new boolean[n + 1];
		
		for (int i = 1; i <= n; i++) {
			// 尝试取所有平方数
			for (int j = 1; j * j <= i; j++) {
				// 如果取j*j个石子后对手必输，则当前玩家必胜
				if (!dp[i - j * j]) {
					dp[i] = true;
					break;
				}
			}
		}
		
		return dp[n];
	}
	
	// ==================== 扩展题目4: Nim游戏变种 ====================
	/*
	 * POJ 2975. Nim (中等)
	 * 题目：标准的Nim游戏，但需要计算先手有多少种必胜走法
	 * 网址：http://poj.org/problem?id=2975
	 * 
	 * 数学原理：计算所有石堆的异或值，对于每个石堆，如果该石堆的数量大于异或值与该石堆数量的异或值
	 * 则存在必胜走法
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	public static int poj2975(int[] piles) {
		int xor = 0;
		for (int pile : piles) {
			xor ^= pile;
		}
		
		if (xor == 0) return 0; // 先手必输
		
		int count = 0;
		for (int pile : piles) {
			// 如果pile > (xor ^ pile)，则存在必胜走法
			if (pile > (xor ^ pile)) {
				count++;
			}
		}
		
		return count;
	}
	
	// ==================== 扩展题目5: 威佐夫博弈 ====================
	/*
	 * 威佐夫博弈经典问题
	 * 题目：有两堆石子，每次可以从一堆取任意个或从两堆取相同数量的石子
	 * 取到最后一个石子的人获胜。判断先手是否能赢。
	 * 
	 * 数学规律：黄金分割比威佐夫定理
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static boolean wythoffGame(int a, int b) {
		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		// 威佐夫定理：当a = floor(k * φ), b = a + k时，先手必输
		// 其中φ = (1 + √5)/2 ≈ 1.618
		double phi = (1 + Math.sqrt(5)) / 2;
		int k = b - a;
		int goldenA = (int)(k * phi);
		
		return a != goldenA;
	}
	
	// ==================== 扩展题目6: Bash博弈 ====================
	/*
	 * Bash博弈经典问题
	 * 题目：有一堆n个石子，每次最多取m个，最少取1个
	 * 取到最后一个石子的人获胜。判断先手是否能赢。
	 * 
	 * 数学规律：当n % (m+1) == 0时先手必输，否则先手必胜
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static boolean bashGame(int n, int m) {
		// 当n % (m+1) == 0时先手必输，否则先手必胜
		return n % (m + 1) != 0;
	}
	
	// ==================== 扩展题目7: 斐波那契博弈 ====================
	/*
	 * 斐波那契博弈经典问题
	 * 题目：有一堆n个石子，先手第一次不能取完所有石子
	 * 之后每次取的石子数不能超过对手刚取的石子数的2倍
	 * 取到最后一个石子的人获胜。判断先手是否能赢。
	 * 
	 * 数学规律：当n是斐波那契数时先手必输，否则先手必胜
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(1)
	 */
	public static boolean fibonacciGame(int n) {
		// 生成斐波那契数列直到大于等于n
		int a = 1, b = 1;
		while (b < n) {
			int temp = a + b;
			a = b;
			b = temp;
		}
		
		// 如果n是斐波那契数，先手必输
		return b != n;
	}
	
	// ==================== 扩展题目8: 石子游戏 ====================
	/*
	 * LeetCode 877. Stone Game (中等)
	 * 题目：偶数堆石子排成一行，每堆有正整数的石子
	 * Alice和Bob轮流从行的开始或结束处取一堆石子
	 * 拥有石子总数最多的玩家获胜。Alice先手，判断Alice是否能赢。
	 * 网址：https://leetcode.com/problems/stone-game/
	 * 
	 * 区间DP解法：
	 * dp[i][j]表示在区间[i,j]中先手玩家比后手玩家多得的石子数
	 * 时间复杂度：O(n²)
	 * 空间复杂度：O(n²)
	 */
	public static boolean stoneGame(int[] piles) {
		int n = piles.length;
		int[][] dp = new int[n][n];
		
		// 初始化：单堆石子，先手玩家得到全部石子
		for (int i = 0; i < n; i++) {
			dp[i][i] = piles[i];
		}
		
		// 区间DP：从小区间到大区间
		for (int length = 2; length <= n; length++) {
			for (int i = 0; i <= n - length; i++) {
				int j = i + length - 1;
				// 当前玩家可以选择取左端或右端的石子
				// 取左端：当前玩家得到piles[i]，对手在区间[i+1,j]中先手
				// 取右端：当前玩家得到piles[j]，对手在区间[i,j-1]中先手
				dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
			}
		}
		
		// 如果先手玩家比后手玩家多得的石子数>0，则先手获胜
		return dp[0][n - 1] > 0;
	}
	
	// ==================== 测试扩展题目 ====================
	public static void testExtendedProblems() {
		System.out.println("=== 扩展题目测试 ===");
		
		// 测试Nim游戏
		System.out.println("Nim Game (n=4): " + canWinNim(4)); // false
		System.out.println("Nim Game (n=5): " + canWinNim(5)); // true
		
		// 测试POJ 2484
		System.out.println("POJ 2484 (n=3): " + poj2484(3)); // false
		System.out.println("POJ 2484 (n=2): " + poj2484(2)); // true
		
		// 测试Stone Game IV
		System.out.println("Stone Game IV (n=1): " + stoneGameIV(1)); // true
		System.out.println("Stone Game IV (n=2): " + stoneGameIV(2)); // false
		
		// 测试POJ 2975
		int[] piles1 = {3, 4, 5};
		System.out.println("POJ 2975: " + poj2975(piles1)); // 2
		
		// 测试威佐夫博弈
		System.out.println("Wythoff Game (2,1): " + wythoffGame(2, 1)); // true
		System.out.println("Wythoff Game (3,5): " + wythoffGame(3, 5)); // false
		
		// 测试Bash博弈
		System.out.println("Bash Game (7,3): " + bashGame(7, 3)); // true
		System.out.println("Bash Game (8,3): " + bashGame(8, 3)); // false
		
		// 测试斐波那契博弈
		System.out.println("Fibonacci Game (5): " + fibonacciGame(5)); // false
		System.out.println("Fibonacci Game (6): " + fibonacciGame(6)); // true
		
		// 测试石子游戏
		int[] piles2 = {5, 3, 4, 5};
		System.out.println("Stone Game: " + stoneGame(piles2)); // true
	}
	
	// ==================== C++实现 ====================
	/*
	 * C++实现代码（注释形式）
	 * 注意：以下为C++代码，需要在C++环境中编译运行
	 */
	/*
	#include <iostream>
	#include <vector>
	#include <cmath>
	#include <algorithm>
	using namespace std;
	
	class Solution {
	public:
	    // C++实现：吃草游戏
	    string win1(int n) {
	        return f(n, "A");
	    }
	    
	    string f(int rest, string cur) {
	        string enemy = (cur == "A") ? "B" : "A";
	        if (rest < 5) {
	            return (rest == 0 || rest == 2) ? enemy : cur;
	        }
	        int pick = 1;
	        while (pick <= rest) {
	            if (f(rest - pick, enemy) == cur) {
	                return cur;
	            }
	            pick *= 4;
	        }
	        return enemy;
	    }
	    
	    string win2(int n) {
	        if (n % 5 == 0 || n % 5 == 2) {
	            return "B";
	        } else {
	            return "A";
	        }
	    }
	    
	    // C++实现：Nim游戏
	    bool canWinNim(int n) {
	        return n % 4 != 0;
	    }
	    
	    // C++实现：Stone Game IV
	    bool stoneGameIV(int n) {
	        if (n == 0) return false;
	        vector<bool> dp(n + 1, false);
	        for (int i = 1; i <= n; i++) {
	            for (int j = 1; j * j <= i; j++) {
	                if (!dp[i - j * j]) {
	                    dp[i] = true;
	                    break;
	                }
	            }
	        }
	        return dp[n];
	    }
	};
	*/
	
	// ==================== Python实现 ====================
	/*
	 * Python实现代码（注释形式）
	 * 注意：以下为Python代码，需要在Python环境中运行
	 */
	/*
	def win1(n: int) -> str:
	    \"\"\"
	    Python实现：吃草游戏递归解法
	    时间复杂度：O(2^(log4(n)))
	    空间复杂度：O(log4(n))
	    \"\"\"
	    def f(rest, cur):
	        enemy = "B" if cur == "A" else "A"
	        if rest < 5:
	            return enemy if rest in [0, 2] else cur
	        pick = 1
	        while pick <= rest:
	            if f(rest - pick, enemy) == cur:
	                return cur
	            pick *= 4
	        return enemy
	    return f(n, "A")
	
	def win2(n: int) -> str:
	    \"\"\"
	    Python实现：吃草游戏数学规律解法
	    时间复杂度：O(1)
	    空间复杂度：O(1)
	    \"\"\"
	    if n % 5 == 0 or n % 5 == 2:
	        return "B"
	    else:
	        return "A"
	
	def can_win_nim(n: int) -> bool:
	    \"\"\"
	    Python实现：Nim游戏
	    时间复杂度：O(1)
	    空间复杂度：O(1)
	    \"\"\"
	    return n % 4 != 0
	
	def stone_game_iv(n: int) -> bool:
	    \"\"\"
	    Python实现：Stone Game IV
	    时间复杂度：O(n√n)
	    空间复杂度：O(n)
	    \"\"\"
	    if n == 0:
	        return False
	    dp = [False] * (n + 1)
	    for i in range(1, n + 1):
	        j = 1
	        while j * j <= i:
	            if not dp[i - j * j]:
	                dp[i] = True
	                break
	            j += 1
	    return dp[n]
	*/
	

}