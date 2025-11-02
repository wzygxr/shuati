package class141;

// 中国剩余定理模版
// 给出n个同余方程，求满足同余方程的最小正数解x
// 一共n个同余方程，x ≡ ri(% mi)
// 1 <= n <= 10
// 0 <= ri、mi <= 10^5
// 所有mi一定互质
// 所有mi整体乘积 <= 10^18
// 测试链接 : https://www.luogu.com.cn/problem/P1495
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 中国剩余定理（Chinese Remainder Theorem, CRT）完整解析与实现
 * 
 * 算法原理详解：
 * 中国剩余定理用于求解如下形式的一元线性同余方程组（其中m1,m2,...,mk两两互质）：
 * x ≡ a1 (mod m1)
 * x ≡ a2 (mod m2)
 * ...
 * x ≡ ak (mod mk)
 * 
 * 数学本质：当模数两两互质时，方程组在模M(所有模数的乘积)意义下存在唯一解
 * 
 * 解法步骤详解：
 * 1. 计算所有模数的乘积 M = m1 * m2 * ... * mk
 * 2. 对于第i个方程：
 *    a. 计算 Mi = M / mi（即排除第i个模数后的乘积）
 *    b. 计算 Mi 在模 mi 意义下的逆元 Mi^(-1)，满足 Mi * Mi^(-1) ≡ 1 (mod mi)
 *    c. 计算 ci = Mi * Mi^(-1)，此时 ci ≡ 1 (mod mi)，且 ci ≡ 0 (mod mj) 当j≠i时
 * 3. 方程组在模 M 意义下的唯一解为 x = (a1*c1 + a2*c2 + ... + ak*ck) mod M
 * 
 * 为什么这是最优解：
 * - 时间复杂度：O(n² log max(mi))，对于给定的问题规模，这是理论最优的时间复杂度
 * - 空间复杂度：O(n)，只需要存储模数和余数数组
 * - 算法正确性：数学定理保证，当模数两两互质时，该解法必然得到唯一解
 * - 实现简洁高效，无需额外的数据结构
 * 
 * 相关题目及详细解析：
 * 
 * 1. 洛谷 P1495【模板】中国剩余定理（CRT）/ 曹冲养猪
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
 *    解题思路：标准的中国剩余定理模板题，直接应用CRT公式求解
 *    代码实现：直接使用本类中的crt方法
 * 
 * 2. 51Nod 1079 中国剩余定理
 *    链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 *    题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
 *    解题思路：题目保证所有模数都是质数，所以两两互质，直接应用CRT
 *    注意点：输入中质数可能重复，需要先合并相同质数的条件
 * 
 * 3. POJ 1006 Biorhythms
 *    链接：http://poj.org/problem?id=1006
 *    题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数
 *    解题思路：三个生理周期分别为23、28、33天，它们两两互质，可以直接应用中国剩余定理，注意结果必须大于给定的日期d
 *    特殊处理：如果结果为0，需要返回M（所有模数的乘积）
 * 
 * 4. UVA 756 Biorhythms
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：与POJ 1006相同
 * 
 * 5. CodeChef - CHEFADV
 *    链接：https://www.codechef.com/problems/CHEFADV
 *    题目大意：判断是否能在一个棋盘上从左上角走到右下角，每次可以走a步或b步
 *    解题思路：可转化为求解模a和模b的同余方程
 * 
 * 6. CodeChef - COPRIME3
 *    链接：https://www.codechef.com/problems/COPRIME3
 *    相关思想：使用同余关系解决数论问题
 * 
 * 7. 牛客网 - NC15857 同余方程
 *    链接：https://ac.nowcoder.com/acm/problem/15857
 *    题目大意：求解同余方程组，模数两两互质
 * 
 * 8. SPOJ - CRTMOD
 *    链接：https://www.spoj.com/problems/CRTMOD/
 *    题目大意：求解多个CRT问题，模数两两互质
 * 
 * 9. HDU 1370 Biorhythms
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=1370
 *    题目大意：与POJ 1006相同，是生物节律问题的复现
 * 
 * 10. Comet OJ - Contest #0 C. 数论问题
 *    链接：https://cometoj.com/contest/0/problem/C
 *    题目大意：求解多个同余方程，模数两两互质
 * 
 * 11. AtCoder Regular Contest 109 B. log
 *    链接：https://atcoder.jp/contests/arc109/tasks/arc109_b
 *    相关思想：涉及模数分解和同余分析
 * 
 * 时间复杂度详细分析：
 * - 中国剩余定理的时间复杂度主要由两部分组成：
 *   1. 计算所有模数的乘积：O(n)
 *   2. 对每个方程计算逆元和加权和：每个方程需要调用一次扩展欧几里得算法
 * - 扩展欧几里得算法的时间复杂度为O(log mi)
 * - 龟速乘法的时间复杂度为O(log mi)
 * - 因此，总的时间复杂度为O(n log max(mi) + n log max(mi)) = O(n log max(mi))
 * - 注意：在某些资料中写为O(n² log max(mi))，这是当n接近max(mi)时的最坏情况估计
 * - 空间复杂度为O(n)，主要用于存储模数和余数数组
 * 
 * 适用场景识别技巧：
 * 1. 当问题描述中涉及多个周期性条件时，考虑使用CRT
 * 2. 当需要求解满足多个同余条件的最小正整数时，考虑使用CRT
 * 3. 当问题中的模数两两互质时，直接使用标准CRT；否则需要使用EXCRT
 * 4. 在密码学、调度问题、周期性模式识别中特别有用
 * 
 * 异常场景与边界处理：
 * 1. 模数为0：输入校验时需要排除模数为0的情况
 * 2. 模数为1：此时余数必须为0，需要特殊处理
 * 3. 负数余数：需要将其转换为正数范围内的等价余数
 * 4. 乘积溢出：使用龟速乘法防止大数乘法溢出
 * 5. 解为0：可能需要返回模数的乘积（根据具体问题要求）
 * 
 * 工程化考量深度分析：
 * 1. 输入校验：
 *    - 检查模数是否为0
 *    - 验证模数是否两两互质（对于CRT来说是必要条件）
 *    - 检查余数是否在有效范围内
 * 
 * 2. 异常处理：
 *    - 定义清晰的错误码或异常类型
 *    - 提供详细的错误信息，指明问题所在
 *    - CRT理论上总是有解，但需要处理实现中的异常情况
 * 
 * 3. 大数处理：
 *    - Java中使用long类型，最大可处理9*10^18
 *    - 超过long范围时需要使用BigInteger类
 *    - 实现龟速乘法以避免乘法溢出
 * 
 * 4. 性能优化：
 *    - 预计算模数的乘积
 *    - 优化扩展欧几里得算法的实现
 *    - 对于已知模数互质的场景，跳过互质性检查
 * 
 * 5. 代码复用性：
 *    - 将核心算法封装为独立函数
 *    - 提供清晰的参数说明和返回值定义
 *    - 编写单元测试覆盖各种场景
 * 
 * 跨语言实现差异分析：
 * 1. Java实现：
 *    - 使用long类型存储大数
 *    - 函数参数传递需要注意数组引用
 *    - 输入输出使用BufferedReader和PrintWriter提高效率
 * 
 * 2. C++实现：
 *    - 需要注意数据类型范围，使用long long
 *    - 函数参数传递更灵活，可以使用引用
 *    - 输入输出可以使用scanf/printf或cin/cout
 * 
 * 3. Python实现：
 *    - 天然支持大整数，无需担心溢出
 *    - 函数实现更简洁，无需显式处理溢出
 *    - 递归深度限制可能影响扩展欧几里得算法（Python默认递归深度约为1000）
 * 
 * 调试技巧与问题定位：
 * 1. 打印中间变量：在关键步骤打印中间结果，如逆元计算结果
 * 2. 验证逆元：检查Mi * Mi^(-1) mod mi是否等于1
 * 3. 验证解：将得到的解代入原方程组验证是否满足所有条件
 * 4. 小数据测试：使用手动计算的小例子验证算法正确性
 * 5. 边界测试：测试模数为1、余数为0等边界情况
 * 
 * 与其他算法的关联与应用：
 * 1. 扩展欧几里得算法：CRT的核心子过程，用于求解逆元
 * 2. 快速幂算法：当模数为质数时，可用于快速计算逆元（费马小定理）
 * 3. 扩展中国剩余定理：处理模数不互质的情况
 * 4. RSA加密算法：使用CRT加速解密过程
 * 5. 多线程计算：将大整数计算分解到多个模数下并行处理
 * 
 * 与机器学习和深度学习的联系：
 * 1. 周期性特征提取：在时间序列分析中识别周期性模式
 * 2. 模数分解技术：在深度学习中的某些优化算法中应用
 * 3. 分布式训练：任务调度中的周期性同步问题
 * 4. 哈希函数设计：利用模运算和同余关系设计高效哈希函数
 */

/* C++实现

#include <iostream>
#include <vector>
using namespace std;

typedef long long ll;

// 扩展欧几里得算法
ll exgcd(ll a, ll b, ll &x, ll &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    ll gcd = exgcd(b, a % b, y, x);
    y -= a / b * x;
    return gcd;
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

// 中国剩余定理求解函数
ll crt(int n, ll m[], ll r[]) {
    ll lcm = 1;
    for (int i = 0; i < n; i++) {
        lcm *= m[i];
    }
    ll ai, ci, ans = 0;
    ll x, y;
    for (int i = 0; i < n; i++) {
        // 计算Mi = lcm / m[i]
        ai = lcm / m[i];
        // 计算Mi的逆元
        exgcd(ai, m[i], x, y);
        // 确保x为正数
        x = (x % m[i] + m[i]) % m[i];
        // 计算ci = r[i] * ai * x % lcm
        ci = multiply(r[i], multiply(ai, x, lcm), lcm);
        ans = (ans + ci) % lcm;
    }
    // 确保结果为正数
    return (ans % lcm + lcm) % lcm;
}

int main() {
    int n;
    cin >> n;
    vector<ll> m(n), r(n);
    for (int i = 0; i < n; i++) {
        cin >> m[i] >> r[i];
    }
    cout << crt(n, m.data(), r.data()) << endl;
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

# 中国剩余定理求解函数
def crt(m_list, r_list):
    """
    中国剩余定理求解函数
    m_list: 模数列表
    r_list: 余数列表
    返回：最小正整数解
    """
    n = len(m_list)
    # 计算所有模数的乘积
    lcm = 1
    for mi in m_list:
        lcm *= mi
    
    ans = 0
    for i in range(n):
        mi = m_list[i]
        ri = r_list[i]
        # 计算Mi = lcm / mi
        ai = lcm // mi
        # 计算Mi的逆元
        gcd, x, y = exgcd(ai, mi)
        # 确保x为正数
        x = (x % mi + mi) % mi
        # 计算ci = ri * ai * x % lcm
        ci = (ri * ai % lcm) * x % lcm
        ans = (ans + ci) % lcm
    
    # 确保结果为正数
    return (ans % lcm + lcm) % lcm

# 输入处理
def main():
    import sys
    n = int(sys.stdin.readline())
    m = []
    r = []
    for _ in range(n):
        mi, ri = map(int, sys.stdin.readline().split())
        m.append(mi)
        r.append(ri)
    print(crt(m, r))

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

public class Code01_CRT {

	// 定义最大数组大小
	public static int MAXN = 11;

	// 存储模数的数组
	public static long m[] = new long[MAXN];

	// 存储余数的数组
	public static long r[] = new long[MAXN];
    
    // 存储扩展欧几里得算法的结果
    // d: 最大公约数，x, y: 贝祖系数，px, py: 上一轮的贝祖系数
	public static long d, x, y, px, py;

	/**
	 * 中国剩余定理求解函数
	 * @param n 方程个数
	 * @return 满足所有同余方程的最小正整数解
	 * @throws IllegalArgumentException 当输入不合法时抛出
	 */
	public static long crt(int n) {
		// 输入校验
		if (n <= 0 || n >= MAXN) {
			throw new IllegalArgumentException("方程个数必须在1到" + (MAXN-1) + "之间");
		}
        
        // 验证模数是否两两互质
        if (!areCoprime(m, n)) {
            throw new IllegalArgumentException("模数必须两两互质，请使用扩展中国剩余定理");
        }
		
		// 计算所有模数的乘积
		long lcm = 1;
		for (int i = 1; i <= n; i++) {
			if (m[i] <= 0) {
				throw new IllegalArgumentException("模数必须为正整数");
			}
			lcm = lcm * m[i];
			// 检查乘积是否溢出
			if (lcm < 0) {
				throw new ArithmeticException("模数乘积溢出，请使用BigInteger版本");
			}
		}
		
		long ai, ci, ans = 0;
		for (int i = 1; i <= n; i++) {
			// 计算Mi = lcm / m[i]
			ai = lcm / m[i];
			// 计算Mi的逆元
			exgcd(ai, m[i]);
			// 确保逆元为正数
			x = (x % m[i] + m[i]) % m[i];
			// 计算ci = (ri * ai * ai逆元) % lcm
			ci = multiply(r[i], multiply(ai, x, lcm), lcm);
			ans = (ans + ci) % lcm;
		}
		
		// 确保结果为正数
		return (ans % lcm + lcm) % lcm;
	}
    
    /**
     * 检查数组中的前n个元素是否两两互质
     * @param arr 输入数组
     * @param n 检查的元素个数
     * @return 如果两两互质返回true，否则返回false
     */
    private static boolean areCoprime(long[] arr, int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                // 计算最大公约数
                exgcd(arr[i], arr[j]);
                if (d != 1) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * BigInteger版本的CRT，用于处理更大的数值范围
     * @param moduli 模数列表
     * @param remainders 余数列表
     * @return 满足所有同余方程的最小正整数解
     */
    public static BigInteger crtBigInteger(BigInteger[] moduli, BigInteger[] remainders) {
        int n = moduli.length;
        BigInteger M = BigInteger.ONE;
        
        // 计算所有模数的乘积
        for (int i = 0; i < n; i++) {
            M = M.multiply(moduli[i]);
        }
        
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < n; i++) {
            BigInteger mi = moduli[i];
            BigInteger ai = M.divide(mi);
            
            // 计算ai的逆元
            BigInteger[] gcdResult = extendedEuclidean(ai, mi);
            BigInteger gcd = gcdResult[0];
            BigInteger xi = gcdResult[1];
            
            // 确保逆元为正数
            xi = xi.mod(mi);
            if (xi.signum() == -1) {
                xi = xi.add(mi);
            }
            
            // 计算ci = ri * ai * xi mod M
            BigInteger ci = remainders[i].multiply(ai).mod(M);
            ci = ci.multiply(xi).mod(M);
            result = result.add(ci).mod(M);
        }
        
        // 确保结果为正数
        return result.mod(M).signum() >= 0 ? result.mod(M) : result.mod(M).add(M);
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
	 * 龟速乘法，防止大数乘法溢出
	 * 利用位运算实现二进制乘法，每一步都取模
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
				ans = (ans + a) % mod;
			}
			// a翻倍
			a = (a + a) % mod;
			// b右移一位
			b >>= 1;
		}
		return ans;
	}
    
    /**
     * 测试函数，验证CRT算法的正确性
     */
    public static void testCRT() {
        // 测试用例1：物不知数问题
        // x ≡ 2 (mod 3)
        // x ≡ 3 (mod 5)
        // x ≡ 2 (mod 7)
        // 预期结果：23
        m[1] = 3; r[1] = 2;
        m[2] = 5; r[2] = 3;
        m[3] = 7; r[3] = 2;
        long result1 = crt(3);
        System.out.println("测试用例1结果: " + result1 + "，预期结果: 23，" + (result1 == 23 ? "通过" : "失败"));
        
        // 验证解的正确性
        boolean valid = (result1 % 3 == 2) && (result1 % 5 == 3) && (result1 % 7 == 2);
        System.out.println("解的验证: " + (valid ? "正确" : "错误"));
    }

	public static void main(String[] args) throws IOException {
		// 可选：运行测试用例
		// testCRT();
		
		// 读取输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		
		// 读取每个方程的模数和余数
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			m[i] = (long) in.nval;
			in.nextToken();
			r[i] = (long) in.nval;
		}
		
		// 调用CRT算法求解
		try {
			long result = crt(n);
			out.println(result);
		} catch (Exception e) {
			// 错误处理
			out.println("错误: " + e.getMessage());
			e.printStackTrace();
		}
		
		// 关闭输入输出流
		out.flush();
		out.close();
		br.close();
	}

}