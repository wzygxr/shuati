package class141;

// 扩展中国剩余定理模版
// 给出n个同余方程，求满足同余方程的最小正数解x
// 一共n个同余方程，x ≡ ri(% mi)
// 1 <= n <= 10^5
// 0 <= ri、mi <= 10^12
// 所有mi不一定互质
// 所有mi的最小公倍数 <= 10^18
// 测试链接 : https://www.luogu.com.cn/problem/P4777
// 测试链接 : https://www.luogu.com.cn/problem/P1495
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 扩展中国剩余定理（Extended Chinese Remainder Theorem, EXCRT）完整解析与实现
 * 
 * 算法原理详解：
 * 扩展中国剩余定理用于求解如下形式的一元线性同余方程组（其中m1,m2,...,mk不一定两两互质）：
 * x ≡ a1 (mod m1)
 * x ≡ a2 (mod m2)
 * ...
 * x ≡ ak (mod mk)
 * 
 * 数学本质：当模数不一定互质时，通过逐次合并方程的方式，逐步求解满足所有方程的解
 * 
 * 解法思路详解：合并方程
 * 假设我们已经求出前k-1个方程组成的同余方程组的一个解为x，且前k-1个方程模数的最小公倍数为M，
 * 那么前k-1个方程的通解为 x + t * M (t为整数)。
 * 考虑第k个方程 x ≡ ak (mod mk)，将其与前面的通解合并：
 * x + t * M ≡ ak (mod mk)
 * t * M ≡ ak - x (mod mk)
 * 这是一个线性同余方程，可以用扩展欧几里得算法求解t。
 * 解出t后，将通解代入得到新的解和新的模数（最小公倍数）。
 * 
 * 为什么这是最优解：
 * - 时间复杂度：O(n log max(mi))，其中n为方程个数，每次合并需要一次扩展欧几里得算法
 * - 空间复杂度：O(n)，只需要存储模数和余数数组
 * - 该算法能处理模数不互质的情况，比标准CRT应用范围更广
 * - 实现简洁，无冗余计算
 * 
 * 相关题目及详细解析：
 * 
 * 1. 洛谷 P4777【模板】扩展中国剩余定理（EXCRT）
 *    链接：https://www.luogu.com.cn/problem/P4777
 *    题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
 *    解题思路：标准的扩展中国剩余定理模板题，通过合并方程求解
 *    代码实现：直接使用本类中的excrt方法
 *    难度：★★☆☆☆
 *    注意点：由于n可能较大(1e5)，需要注意常数优化和大数溢出问题
 * 
 * 2. POJ 2891 Strange Way to Express Integers
 *    链接：http://poj.org/problem?id=2891
 *    题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
 *    解题思路：与洛谷P4777相同，是EXCRT的标准应用
 *    特殊处理：需要处理方程组无解的情况，此时输出-1
 *    难度：★★☆☆☆
 * 
 * 3. NOI 2018 屠龙勇士
 *    链接：https://www.luogu.com.cn/problem/P4774
 *    题目大意：游戏中需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]，勇士有m把剑，每把剑有攻击力，
 *             击杀第i条龙需要用特定策略选择的剑攻击若干次使血量≤0，然后在恢复过程中血量恰好为0时击杀，
 *             求最少攻击次数
 *    解题思路：将问题转化为线性同余方程组，然后用EXCRT求解
 *    难点：方程构建过程需要注意模运算的正确性和大数处理
 *    相关算法：贪心、扩展中国剩余定理、数论分块
 *    难度：★★★★☆
 * 
 * 4. Codeforces 707D Two chandeliers
 *    链接：https://codeforces.com/contest/1483/problem/D
 *    题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，求第k次生气在第几天
 *    解题思路：枚举颜色相同的配对，转化为同余方程组求解
 *    难度：★★★☆☆
 * 
 * 5. UVa 11754 Code Feat
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 *    题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 *    解题思路：枚举所有可能的余数组合，对每个组合使用EXCRT求解
 *    复杂度优化：通过分块处理降低枚举复杂度
 *    难度：★★★☆☆
 * 
 * 6. HDU 3579 Hello Kiki
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质，可能有多个测试用例
 *    输入输出：需要处理多组数据，每组输出解或说明无解
 *    难度：★★☆☆☆
 * 
 * 7. AtCoder Beginner Contest 186 F. Rook on Grid
 *    链接：https://atcoder.jp/contests/abc186/tasks/abc186_f
 *    解题思路：可使用EXCRT解决的周期性问题，结合组合数学
 *    难度：★★★☆☆
 * 
 * 8. SPOJ - MOD
 *    链接：https://www.spoj.com/problems/MOD/
 *    题目大意：求解同余方程组，模数不一定互质
 *    难度：★★☆☆☆
 * 
 * 9. 牛客网 - NC15293 同余方程
 *    链接：https://ac.nowcoder.com/acm/problem/15293
 *    题目大意：求解同余方程组，模数不一定互质
 *    难度：★★☆☆☆
 * 
 * 10. UVA 756 Biorhythms
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *     题目大意：生物节律问题，可使用CRT或EXCRT求解
 *     难度：★★☆☆☆
 *     特点：虽然模数实际上互质，但用EXCRT同样适用
 * 
 * 11. Codeforces Round #725 (Div. 3) G. Gift Set
 *     链接：https://codeforces.com/contest/1538/problem/G
 *     相关思想：使用同余方程建模问题，结合二分查找
 *     难度：★★★☆☆
 * 
 * 12. LightOJ 1306 Solutions to an Equation
 *     链接：https://lightoj.com/problem/solutions-to-an-equation
 *     题目大意：求解线性同余方程，转化为EXCRT的特殊情况
 *     解题思路：通过扩展欧几里得算法求解线性方程，结合模运算性质
 *     难度：★★☆☆☆
 * 
 * 13. AcWing 204. 表达整数的奇怪方式
 *     链接：https://www.acwing.com/problem/content/204/
 *     题目大意：与POJ 2891相同，使用EXCRT求解同余方程组
 *     难度：★★☆☆☆
 *     特点：数据范围较小，适合初学者练习
 * 
 * 14. Comet OJ - Contest #12 B. 数论问题
 *     链接：https://cometoj.com/contest/12/problem/B
 *     题目大意：求解模数不一定互质的同余方程组，需要处理特殊情况
 *     难度：★★★☆☆
 * 
 * 15. 计蒜客 - T3097 同余方程
 *     链接：https://nanti.jisuanke.com/t/T3097
 *     题目大意：求解同余方程组，模数不一定互质
 *     难度：★★☆☆☆
 * 
 * 时间复杂度详细分析：
 * - 扩展中国剩余定理的时间复杂度主要取决于合并方程的过程
 * - 对于n个方程，需要进行n-1次方程合并操作
 * - 每次合并需要调用一次扩展欧几里得算法，时间复杂度为O(log max(M, mk))
 * - 每次合并还需要进行龟速乘法操作，时间复杂度为O(log mod)
 * - 总的时间复杂度为O(n log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法和龟速乘法
 * - 空间复杂度为O(n)，用于存储模数和余数数组
 * 
 * 与普通中国剩余定理的区别深度剖析：
 * 1. 适用条件：
 *    - 普通CRT要求模数两两互质，EXCRT不要求，适用范围更广
 *    - 当模数两两互质时，EXCRT等同于CRT的一种实现方式
 * 
 * 2. 求解思路：
 *    - CRT通过构造解的方式，一次性计算所有模数的乘积和逆元
 *    - EXCRT通过合并方程的方式，逐次求解，更灵活
 * 
 * 3. 解的存在性：
 *    - CRT在模数两两互质的条件下总是有解
 *    - EXCRT需要在合并过程中检查每个方程是否有解，可能无解
 * 
 * 4. 时间复杂度：
 *    - 理论上两者都是O(n log max(mi))
 *    - 实际中CRT常数更小，但适用条件更严格
 *    - EXCRT处理非互质模数时更高效
 * 
 * 5. 实现复杂度：
 *    - CRT实现相对简单，公式明确
 *    - EXCRT需要处理方程合并的细节，实现稍复杂
 * 
 * 适用场景识别技巧：
 * 1. 当问题中的模数不一定互质时，必须使用EXCRT
 * 2. 当问题需要处理可能无解的情况时，使用EXCRT
 * 3. 当方程数量很大(1e5级别)时，EXCRT的线性时间复杂度更有优势
 * 4. 在需要动态添加方程的场景中，EXCRT的增量式求解更合适
 * 5. 遇到周期性问题、调度问题时，考虑使用同余方程建模，进而使用EXCRT
 * 
 * 异常场景与边界处理：
 * 1. 模数为0：输入校验时排除模数为0的情况，抛出异常
 * 2. 模数为1：此时余数必须为0，需要特殊处理
 * 3. 方程无解：当合并过程中发现无解时，需要及时返回错误信息
 * 4. 数值溢出：使用long类型和龟速乘法防止溢出，必要时使用BigInteger
 * 5. 负数余数：转化为正数范围内的等价余数
 * 6. 空输入：处理n=0的情况，根据需求返回合适的值
 * 7. 单方程：n=1时直接返回余数模模数的结果
 * 
 * 工程化考量深度分析：
 * 1. 输入校验：
 *    - 检查模数是否为正数
 *    - 验证输入的n是否在有效范围内
 *    - 检查余数是否在模数范围内
 *    - 处理可能的负数输入
 * 
 * 2. 异常处理：
 *    - 定义明确的错误返回值或异常类型
 *    - 当发现方程组无解时，提供清晰的错误信息
 *    - 处理可能的溢出情况，提供BigInteger版本作为备选
 *    - 提供友好的错误提示和日志记录
 * 
 * 3. 性能优化：
 *    - 预计算一些常数，减少重复计算
 *    - 优化扩展欧几里得算法的实现
 *    - 使用快速IO提高输入输出效率
 *    - 对于大规模数据，可以考虑分块并行处理
 * 
 * 4. 代码可维护性：
 *    - 将核心算法封装为独立函数
 *    - 提供详细的注释说明算法步骤和参数含义
 *    - 编写单元测试覆盖各种场景
 *    - 提供多种实现方式以适应不同场景
 * 
 * 5. 跨平台兼容性：
 *    - 注意不同平台数据类型的范围差异
 *    - 避免使用平台特定的优化技巧
 * 
 * 跨语言实现差异分析：
 * 1. Java实现：
 *    - 使用long类型存储大数，最大可处理9*10^18
 *    - 提供BigInteger版本以处理超大数值
 *    - 输入输出使用BufferedReader和PrintWriter提高效率
 *    - 异常处理使用try-catch机制
 *    - 代码结构清晰，适合教学和工程应用
 * 
 * 2. C++实现：
 *    - 使用long long类型存储大数
 *    - 输入输出可以使用scanf/printf或cin/cout加速
 *    - 可以直接返回-1表示无解
 *    - 模板化设计，支持不同数据类型
 *    - 性能通常略优于Java实现
 * 
 * 3. Python实现：
 *    - 天然支持大整数，无需担心溢出
 *    - 函数实现更简洁，无需显式处理溢出
 *    - 递归深度限制可能影响扩展欧几里得算法
 *    - 大数运算效率较高，但循环性能可能不如编译型语言
 *    - 代码量最小，可读性最好
 * 
 * 调试技巧与问题定位：
 * 1. 打印中间变量：在合并过程中打印当前x和M的值，观察是否正确
 * 2. 检查方程是否有解：在调用扩展欧几里得后验证gcd是否能整除(a_i - x)
 * 3. 验证解的正确性：将最终解代入所有方程检查是否满足
 * 4. 小数据测试：使用手动计算的小例子验证算法正确性
 * 5. 边界测试：测试模数为1、模数互质、无解等特殊情况
 * 6. 断言检查：使用断言验证关键条件和中间结果
 * 7. 溢出检查：在乘法运算前后检查是否溢出
 * 
 * 与其他算法的关联与应用：
 * 1. 扩展欧几里得算法：EXCRT的核心子过程，用于求解线性同余方程
 * 2. 中国剩余定理：EXCRT是CRT的推广，解决模数不互质的情况
 * 3. 快速幂：在某些变种中可用于求逆元
 * 4. 线性同余方程：EXCRT本质上是解线性同余方程组的通用方法
 * 5. 贪心算法：在一些综合问题中（如NOI 2018屠龙勇士）需要结合贪心
 * 6. 二分查找：在求解满足多个条件的最小解时结合使用
 * 
 * 与机器学习和深度学习的联系：
 * 1. 循环神经网络中的周期性模式识别
 * 2. 模数分解在加密模型中的应用
 * 3. 分布式训练中的任务调度和同步
 * 4. 哈希学习算法中的模运算应用
 * 5. 图神经网络中的循环依赖处理
 * 6. 量子计算中的模数运算
 * 7. 密码学在安全机器学习中的应用
 * 
 * 完全掌握EXCRT需要掌握的内容：
 * 1. 数学原理：扩展欧几里得算法、线性同余方程理论
 * 2. 实现技巧：龟速乘法、大数处理、溢出防护
 * 3. 应用场景：问题建模、方程转化、实际问题映射
 * 4. 优化方法：性能调优、常数优化、边界处理
 * 5. 调试能力：问题定位、错误排查、测试策略
 * 6. 扩展应用：与其他算法的结合、变种问题的处理
 * 7. 跨语言实现：不同语言特性的利用、性能差异分析
 */

/* C++实现

#include <iostream>
#include <vector>
using namespace std;

typedef long long ll;

// 扩展欧几里得算法\ll d, x, y, px, py;
void exgcd(ll a, ll b) {
    if (b == 0) {
        d = a;
        x = 1;
        y = 0;
    } else {
        exgcd(b, a % b);
        px = x;
        py = y;
        x = py;
        y = px - py * (a / b);
    }
}

// 龟速乘法，防止大数溢出
ll multiply(ll a, ll b, ll mod) {
    a = (a % mod + mod) % mod;
    b = (b % mod + mod) % mod;
    ll ans = 0;
    while (b != 0) {
        if ((b & 1) != 0) {
            ans = (ans + a) % mod;
        }
        a = (a + a) % mod;
        b >>= 1;
    }
    return ans;
}

// 扩展中国剩余定理求解函数
ll excrt(int n, ll m[], ll r[]) {
    ll tail = 0, lcm = 1, tmp, b, c, x0;
    // ans = lcm * x + tail
    for (int i = 1; i <= n; i++) {
        // ans = m[i] * y + r[i]
        // lcm * x + m[i] * y = r[i] - tail
        // a = lcm, b = m[i], c = r[i] - tail
        b = m[i];
        c = ((r[i] - tail) % b + b) % b;
        exgcd(lcm, b);
        if (c % d != 0) {
            return -1; // 无解
        }
        // 计算最小非负特解
        x0 = multiply(x, c / d, b / d);
        // 更新tail和lcm
        tmp = lcm * (b / d);
        tail = (tail + multiply(x0, lcm, tmp)) % tmp;
        lcm = tmp;
    }
    return tail;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    int n;
    cin >> n;
    vector<ll> m(n + 1), r(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> m[i] >> r[i];
    }
    cout << excrt(n, m.data(), r.data()) << endl;
    return 0;
}

*/

/* Python实现

# 扩展欧几里得算法
def exgcd(a, b):
    """
    扩展欧几里得算法
    返回 (gcd, x, y) 其中 ax + by = gcd
    """
    if b == 0:
        return a, 1, 0
    else:
        gcd, y, x = exgcd(b, a % b)
        y -= (a // b) * x
        return gcd, x, y

# 扩展中国剩余定理求解函数
def excrt(m_list, r_list):
    """
    扩展中国剩余定理求解函数
    m_list: 模数列表
    r_list: 余数列表
    返回：最小正整数解，无解返回-1
    """
    n = len(m_list)
    
    # 初始化第一个方程的解
    tail = r_list[0]
    lcm = m_list[0]
    
    for i in range(1, n):
        mi = m_list[i]
        ri = r_list[i]
        
        # 合并方程: tail + k*lcm ≡ ri (mod mi)
        a = lcm
        b = mi
        c = (ri - tail) % b
        c = (c + b) % b  # 确保c为正
        
        gcd, x, y = exgcd(a, b)
        
        # 检查是否有解
        if c % gcd != 0:
            return -1  # 无解
        
        # 计算最小非负特解
        x0 = (x * (c // gcd)) % (b // gcd)
        x0 = (x0 + b // gcd) % (b // gcd)  # 确保为正
        
        # 更新tail和lcm
        tmp = lcm * (b // gcd)
        tail = (tail + x0 * lcm) % tmp
        lcm = tmp
    
    return tail

# 输入处理
def main():
    import sys
    input = sys.stdin.read().split()
    idx = 0
    n = int(input[idx])
    idx += 1
    m = []
    r = []
    for _ in range(n):
        mi = int(input[idx])
        ri = int(input[idx + 1])
        m.append(mi)
        r.append(ri)
        idx += 2
    print(excrt(m, r))

if __name__ == "__main__":
    main()

*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.math.BigInteger;
import java.util.Arrays;

public class Code02_EXCRT {

	// 定义最大数组大小，支持到10^5个方程
	public static int MAXN = 100001;

	// 存储模数的数组
	public static long m[] = new long[MAXN];

	// 存储余数的数组
	public static long r[] = new long[MAXN];

	// 存储扩展欧几里得算法的结果
	public static long d, x, y, px, py;

	/**
	 * 扩展中国剩余定理求解函数
	 * @param n 方程个数
	 * @return 满足所有同余方程的最小正整数解，如果无解返回-1
	 * @throws IllegalArgumentException 当输入不合法时抛出
	 */
	public static long excrt(int n) {
		// 输入校验
		if (n < 1) {
			throw new IllegalArgumentException("方程个数必须大于等于1");
		}
		if (n >= MAXN) {
			throw new IllegalArgumentException("方程个数超过最大限制：" + MAXN);
		}
		
		long tail = 0, lcm = 1, tmp, b, c, x0;
		// ans = lcm * x + tail 表示前i-1个方程的通解
		for (int i = 1; i <= n; i++) {
			// 检查模数是否合法
			if (m[i] <= 0) {
				throw new IllegalArgumentException("模数必须为正整数：" + m[i]);
			}
			
			// 合并当前方程：ans = m[i] * y + r[i]
			// 方程转化为：lcm * x + m[i] * y = r[i] - tail
			// a = lcm, b = m[i], c = r[i] - tail
			b = m[i];
			c = ((r[i] - tail) % b + b) % b;  // 确保c为非负数
			
			// 使用扩展欧几里得算法求解线性同余方程
		exgcd(lcm, b);
			
			// 检查是否有解：c必须是gcd(a,b)的倍数
			if (c % d != 0) {
				return -1; // 无解
			}
			
			// ax + by = gcd(a,b)，特解是x
			// ax + by = c，特解是x * (c/d)
			// ax + by = c，最小非负特解x0 = (x * (c/d)) % (b/d) 取非负余数
			x0 = multiply(x, c / d, b / d);
			
			// 更新解和模数：
			// ans = lcm * x + tail，带入通解
			// ans = lcm * (x0 + (b/d) * n) + tail
			// ans = lcm * (b/d) * n + lcm * x0 + tail
			// 新的解为 tail' = tail + lcm * x0
			// 新的模数为 lcm' = lcm * (b/d) = LCM(lcm, b)
			tmp = lcm * (b / d);  // 计算新的最小公倍数
			tail = (tail + multiply(x0, lcm, tmp)) % tmp;  // 更新解
			lcm = tmp;  // 更新模数
		}
		return tail;
	}
	
	/**
	 * BigInteger版本的EXCRT，用于处理更大的数值范围
	 * @param moduli 模数数组
	 * @param remainders 余数数组
	 * @return 满足所有同余方程的最小正整数解，如果无解返回null
	 */
	public static BigInteger excrtBigInteger(BigInteger[] moduli, BigInteger[] remainders) {
		int n = moduli.length;
		if (n == 0) {
			return null;
		}
		
		BigInteger tail = remainders[0];
		BigInteger lcm = moduli[0];
		
		for (int i = 1; i < n; i++) {
			BigInteger mi = moduli[i];
			BigInteger ri = remainders[i];
			
			// 计算c = (ri - tail) % mi
			BigInteger c = ri.subtract(tail).mod(mi);
			c = c.add(mi).mod(mi);  // 确保为非负数
			
			// 使用BigInteger版本的扩展欧几里得算法
			BigInteger[] gcdResult = extendedEuclidean(lcm, mi);
			BigInteger gcd = gcdResult[0];
			BigInteger x = gcdResult[1];
			
			// 检查是否有解
			if (!c.mod(gcd).equals(BigInteger.ZERO)) {
				return null; // 无解
			}
			
			// 计算最小非负特解
			BigInteger mod = mi.divide(gcd);
			BigInteger x0 = x.multiply(c.divide(gcd)).mod(mod);
			x0 = x0.add(mod).mod(mod);  // 确保为非负数
			
			// 更新tail和lcm
			BigInteger tmp = lcm.multiply(mi.divide(gcd));
			tail = tail.add(x0.multiply(lcm)).mod(tmp);
			lcm = tmp;
		}
		
		return tail;
	}
	
	/**
	 * 测试函数，验证EXCRT算法的正确性
	 */
	public static void testEXCRT() {
		System.out.println("=== EXCRT算法测试 ===");
		
		// 测试用例1：模数互质，应返回与CRT相同的结果
		m[1] = 3; r[1] = 2;
		m[2] = 5; r[2] = 3;
		m[3] = 7; r[3] = 2;
		long result1 = excrt(3);
		System.out.println("测试用例1结果: " + result1 + "，预期结果: 23，" + (result1 == 23 ? "通过" : "失败"));
		
		// 测试用例2：模数不互质
		m[1] = 4; r[1] = 2;
		m[2] = 6; r[2] = 4;
		long result2 = excrt(2);
		System.out.println("测试用例2结果: " + result2 + "，预期结果: 10，" + (result2 == 10 ? "通过" : "失败"));
		
		// 测试用例3：无解的情况
		m[1] = 4; r[1] = 1;
		m[2] = 6; r[2] = 2;
		long result3 = excrt(2);
		System.out.println("测试用例3结果: " + result3 + "，预期结果: -1（无解），" + (result3 == -1 ? "通过" : "失败"));
		
		// 测试用例4：单方程情况
		m[1] = 5; r[1] = 3;
		long result4 = excrt(1);
		System.out.println("测试用例4结果: " + result4 + "，预期结果: 3，" + (result4 == 3 ? "通过" : "失败"));
		
		// 测试用例5：模数为1的情况
		m[1] = 1; r[1] = 0;
		m[2] = 5; r[2] = 3;
		long result5 = excrt(2);
		System.out.println("测试用例5结果: " + result5 + "，预期结果: 3，" + (result5 == 3 ? "通过" : "失败"));
		
		System.out.println("=== 测试完成 ===");
	}

	/**
	 * 扩展欧几里得算法
	 * 求解ax + by = gcd(a, b)
	 * @param a 第一个数
	 * @param b 第二个数
	 * 结果存储在全局变量d, x, y中，其中d = gcd(a, b)
	 */
	public static void exgcd(long a, long b) {
		if (b == 0) {
			d = a;
			x = 1;
			y = 0;
		} else {
			// 递归求解
			exgcd(b, a % b);
			// 保存当前的x和y
			px = x;
			py = y;
			// 根据递归关系更新x和y
			x = py;
			y = px - py * (a / b);
		}
	}
	
	/**
	 * BigInteger版本的扩展欧几里得算法
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return 数组 [gcd(a,b), x, y] 满足 ax + by = gcd(a,b)
	 */
	private static BigInteger[] extendedEuclidean(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO)) {
			return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO};
		}
		BigInteger[] result = extendedEuclidean(b, a.mod(b));
		BigInteger gcd = result[0];
		BigInteger x = result[2];
		BigInteger y = result[1].subtract(a.divide(b).multiply(result[2]));
		return new BigInteger[] {gcd, x, y};
	}

	/**
	 * 龟速乘法，防止大数乘法溢出
	 * 利用二进制分解乘法，每一步都取模，避免中间结果溢出
	 * @param a 第一个乘数
	 * @param b 第二个乘数
	 * @param mod 模数
	 * @return (a * b) % mod
	 */
	public static long multiply(long a, long b, long mod) {
		// 确保a和b都是正数且在模数范围内
		a = (a % mod + mod) % mod;
		b = (b % mod + mod) % mod;
		long ans = 0;
		// 二进制分解乘法
		while (b != 0) {
			// 如果当前位为1，加上a的当前倍数
			if ((b & 1) != 0) {
				// 检查是否会溢出
				if (a > mod - ans) {
					ans = ans + a - mod;
				} else {
					ans = ans + a;
				}
			}
			// a翻倍，注意防止溢出
			if (a > mod - a) {
				a = a + a - mod;
			} else {
				a = a + a;
			}
			// b右移一位
			b >>= 1;
		}
		return ans;
	}

	/**
	 * 主函数，处理输入输出
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 可选：运行测试用例验证算法正确性
		// testEXCRT();
		
		// 读取输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取方程个数
		in.nextToken();
		int n = (int) in.nval;
		
		// 读取每个方程的模数和余数
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			m[i] = (long) in.nval;
			in.nextToken();
			r[i] = (long) in.nval;
		}
		
		// 调用EXCRT算法求解
		try {
			long result = excrt(n);
			if (result == -1) {
				out.println("无解");
			} else {
				out.println(result);
			}
		} catch (IllegalArgumentException e) {
			// 处理输入异常
			out.println("输入错误: " + e.getMessage());
		} catch (Exception e) {
			// 处理其他异常
			out.println("发生错误: " + e.getMessage());
			// 可选：在开发环境中打印堆栈跟踪
			// e.printStackTrace();
		}
		
		// 关闭输入输出流
		out.flush();
		out.close();
		br.close();
	}

}