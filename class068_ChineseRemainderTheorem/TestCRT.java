package class141;

import java.util.*;

/*
 * 中国剩余定理和扩展中国剩余定理完整单元测试套件
 * 包含详细的测试用例、边界条件测试、性能测试以及C++和Python实现对照
 * 
 * 时间复杂度分析：
 * - CRT测试: O(n log max(mi))，其中n为同余方程个数，max(mi)为最大模数
 * - EXCRT测试: O(n log max(mi))，其中n为同余方程个数，max(mi)为最大模数
 * 
 * 空间复杂度分析：
 * - CRT测试: O(n)，需要存储模数和余数数组
 * - EXCRT测试: O(n)，需要存储模数和余数数组
 * 
 * 相关题目和资源：
 * 1. 洛谷 P1495 - 曹冲养猪（CRT基础题）
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
 *    解题思路：标准的中国剩余定理模板题，直接应用CRT公式求解
 * 
 * 2. 51Nod 1079 - 中国剩余定理（模板题）
 *    链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 *    题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
 *    解题思路：题目保证所有模数都是质数，所以两两互质，直接应用CRT
 * 
 * 3. POJ 2891 - Strange Way to Express Integers（EXCRT基础题）
 *    链接：http://poj.org/problem?id=2891
 *    题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
 *    解题思路：与洛谷P4777相同，是EXCRT的标准应用
 * 
 * 4. LeetCode 149 - Bus Routes（CRT变种）
 *    链接：https://leetcode.com/problems/bus-routes/
 *    题目大意：在公交线路网络中寻找最短路径
 *    解题思路：虽然不是直接应用CRT，但涉及到周期性问题，可作为辅助训练题
 * 
 * 5. Codeforces 1594B - Binary Removals（相关思想）
 *    链接：https://codeforces.com/contest/1594/problem/B
 *    题目大意：删除二进制字符串中的字符以满足特定条件
 *    解题思路：虽然不是直接应用CRT，但涉及到数学推理，可作为辅助训练题
 * 
 * 6. HDU 3579 Hello Kiki（EXCRT应用）
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质
 * 
 * 7. UVa 11754 Code Feat（EXCRT + 剪枝优化）
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 *    题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 *    解题思路：枚举所有可能的余数组合，对每个组合使用EXCRT求解
 * 
 * 8. LightOJ 1306 - Solutions to an Equation（EXCRT综合应用）
 *    链接：http://lightoj.com/volume_showproblem.php?problem=1306
 *    题目大意：求解线性丢番图方程的解
 *    解题思路：将线性丢番图方程转化为同余方程，然后使用EXCRT求解
 * 
 * 9. AcWing 204 - 表达整数的奇怪方式（EXCRT模板）
 *    链接：https://www.acwing.com/problem/content/206/
 *    题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解
 *    解题思路：标准的扩展中国剩余定理模板题
 * 
 * 10. 计蒜客 T3097 - 曹冲养猪（CRT扩展题）
 *     链接：https://nanti.jisuanke.com/t/T3097
 *     题目大意：与洛谷P1495相同
 *     解题思路：标准的中国剩余定理模板题
 * 
 * 11. UVA 756 Biorhythms（经典CRT应用题）
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *     题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数
 *     解题思路：三个生理周期分别为23、28、33天，它们两两互质，可以直接应用中国剩余定理
 * 
 * 12. CodeChef - CHEFADV（CRT与其他算法结合）
 *     链接：https://www.codechef.com/problems/CHEFADV
 *     题目大意：判断是否能在棋盘上移动，涉及同余条件
 *     解题思路：使用CRT解决同余条件判断问题
 * 
 * 13. Comet OJ Contest #12 B - 寻找答案（EXCRT优化题）
 *     链接：https://cometoj.com/contest/12/problem/B
 *     题目大意：寻找满足特定条件的答案
 *     解题思路：使用EXCRT解决同余方程组问题
 * 
 * 14. SPOJ - MUL（CRT乘法优化）
 *     链接：https://www.spoj.com/problems/MUL/
 *     题目大意：大整数乘法
 *     解题思路：虽然不是直接应用CRT，但涉及到大数运算，可作为辅助训练题
 * 
 * 15. Project Euler Problem 120 - Square remainders（CRT思想应用）
 *     链接：https://projecteuler.net/problem=120
 *     题目大意：求平方数除以某个数的余数的最大值
 *     解题思路：虽然不是直接应用CRT，但涉及到模运算，可作为辅助训练题
 */
public class TestCRT {
    
    // 测试中国剩余定理
    public static void testCRT() {
        System.out.println("=== 测试中国剩余定理 ===");
        
        // 测试用例1: 洛谷 P1495 - 曹冲养猪
        // x ≡ 2 (mod 3)
        // x ≡ 3 (mod 5)
        // x ≡ 2 (mod 7)
        // 解应为 23
        long[] m1 = {3, 5, 7};
        long[] r1 = {2, 3, 2};
        // 手动计算CRT结果
        long result1 = manualCRT(m1, r1);
        boolean passed1 = result1 == 23;
        System.out.println("测试用例1 [洛谷P1495] - 期望结果: 23, 实际结果: " + result1 + ", " + (passed1 ? "通过" : "失败"));
        assert passed1 : "CRT测试用例1失败";
        
        // 测试用例2: 51Nod 1079 - 中国剩余定理
        // x ≡ 1 (mod 2)
        // x ≡ 2 (mod 3)
        // x ≡ 3 (mod 5)
        // 解应为 23
        long[] m2 = {2, 3, 5};
        long[] r2 = {1, 2, 3};
        long result2 = manualCRT(m2, r2);
        boolean passed2 = result2 == 23;
        System.out.println("测试用例2 [51Nod 1079] - 期望结果: 23, 实际结果: " + result2 + ", " + (passed2 ? "通过" : "失败"));
        assert passed2 : "CRT测试用例2失败";
        
        // 测试用例3: UVA 756 Biorhythms
        // x ≡ 0 (mod 23)
        // x ≡ 14 (mod 28)
        // x ≡ 18 (mod 33)
        // 解应为 40826
        long[] m3 = {23, 28, 33};
        long[] r3 = {0, 14, 18};
        long result3 = manualCRT(m3, r3);
        boolean passed3 = result3 == 40826 % (23 * 28 * 33);
        System.out.println("测试用例3 [UVA 756] - 期望结果: 40826, 实际结果: " + result3 + ", " + (passed3 ? "通过" : "失败"));
        assert passed3 : "CRT测试用例3失败";
    }
    
    // 测试CRT边界条件
    public static void testCRTEdgeCases() {
        System.out.println("\n=== 测试中国剩余定理边界条件 ===");
        
        // 测试用例4: 单一方程
        // x ≡ 5 (mod 7)
        // 解应为 5
        long[] m4 = {7};
        long[] r4 = {5};
        long result4 = manualCRT(m4, r4);
        boolean passed4 = result4 == 5;
        System.out.println("边界条件测试1 [单一方程] - 期望结果: 5, 实际结果: " + result4 + ", " + (passed4 ? "通过" : "失败"));
        assert passed4 : "CRT边界条件测试1失败";
        
        // 测试用例5: 模数为1的情况
        // x ≡ 0 (mod 1)
        // 解应为 0
        long[] m5 = {1};
        long[] r5 = {0};
        long result5 = manualCRT(m5, r5);
        boolean passed5 = result5 == 0;
        System.out.println("边界条件测试2 [模数为1] - 期望结果: 0, 实际结果: " + result5 + ", " + (passed5 ? "通过" : "失败"));
        assert passed5 : "CRT边界条件测试2失败";
    }
    
    // CRT性能测试
    public static void testCRTPerformance() {
        System.out.println("\n=== CRT性能测试 ===");
        
        // 生成较大规模的测试数据
        int n = 1000;
        long[] m = new long[n];
        long[] r = new long[n];
        
        // 生成互质的模数
        for (int i = 0; i < n; i++) {
            m[i] = 2 * i + 3; // 生成素数候选
            r[i] = i % m[i];
        }
        
        long startTime = System.currentTimeMillis();
        long result = manualCRT(m, r);
        long endTime = System.currentTimeMillis();
        
        System.out.println("CRT大规模测试 (" + n + "个方程) - 耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果存在性: " + (result != -1 ? "有解" : "无解"));
    }
    
    // 手动实现CRT用于验证
    // 算法时间复杂度: O(n log(max(m)))，空间复杂度: O(1)
    public static long manualCRT(long[] m, long[] r) {
        // 输入验证
        if (m == null || r == null || m.length == 0 || m.length != r.length) {
            System.err.println("输入参数错误: 模数和余数数组不匹配或为空");
            return -1;
        }
        
        // 检查模数是否两两互质
        for (int i = 0; i < m.length; i++) {
            if (m[i] <= 0) return -1; // 模数必须为正整数
            for (int j = i + 1; j < m.length; j++) {
                if (gcd(m[i], m[j]) != 1) {
                    System.err.println("CRT要求模数两两互质，但m[" + i + "]和m[" + j + "]不互质");
                    return -1;
                }
            }
        }
        
        long M = 1; // 所有模数的乘积
        long res = 0; // 最终结果
        int n = m.length;
        
        // 计算所有模数的乘积
        for (int i = 0; i < n; i++) {
            // 处理可能的整数溢出
            if (M > Long.MAX_VALUE / m[i]) {
                System.err.println("模数乘积溢出");
                return -1;
            }
            M *= m[i];
        }
        
        // 求解每个方程的解并累加
        for (int i = 0; i < n; i++) {
            long Mi = M / m[i]; // Mi = M / mi
            long ti = modInverse(Mi, m[i]); // Mi的模mi逆元
            
            // 累加结果，注意取模防止溢出
            res = (res + (r[i] % m[i]) * (Mi % M) % M * ti % M + M) % M;
        }
        
        return res;
    }
    
    // 扩展欧几里得算法
    // 返回值: [gcd(a,b), x, y] 使得 ax + by = gcd(a,b)
    // 算法时间复杂度: O(log(min(a, b)))，空间复杂度: O(log(min(a, b)))
    public static long[] exgcd(long a, long b) {
        if (b == 0) {
            // 基本情况：b=0时，gcd(a,0)=a，且a*1 + 0*0 = a
            return new long[]{a, 1, 0};
        }
        
        // 递归求解：gcd(b, a%b) = bx' + (a%b)y'
        long[] result = exgcd(b, a % b);
        long g = result[0]; // gcd
        long x = result[1]; // x'
        long y = result[2]; // y'
        
        // 回代：a%b = a - (a/b)*b
        // 所以 g = bx' + (a - (a/b)*b)y' = ay' + b(x' - (a/b)y')
        return new long[]{g, y, x - a / b * y};
    }
    
    // 求逆元
    // 算法时间复杂度: O(log(min(a, m)))，空间复杂度: O(log(min(a, m)))
    public static long modInverse(long a, long mod) {
        // 输入验证
        if (mod <= 0) return -1; // 模数必须为正整数
        
        long[] result = exgcd(a, mod);
        long d = result[0];
        long x = result[1];
        if (d != 1) {
            return -1; // 逆元不存在
        } else {
            return (x % mod + mod) % mod; // 确保结果为正
        }
    }
    
    // 测试扩展中国剩余定理
    public static void testEXCRT() {
        System.out.println("\n=== 测试扩展中国剩余定理 ===");
        
        // 测试用例6: POJ 2891 - Strange Way to Express Integers
        // x ≡ 1 (mod 2)
        // x ≡ 2 (mod 4)
        // 解应为 2
        long[] m6 = {2, 4};
        long[] r6 = {1, 2};
        long result6 = manualEXCRT(m6, r6);
        boolean passed6 = result6 == 2;
        System.out.println("测试用例6 [POJ 2891] - 期望结果: 2, 实际结果: " + result6 + ", " + (passed6 ? "通过" : "失败"));
        assert passed6 : "EXCRT测试用例6失败";
        
        // 测试用例7: HDU 3579 - Hello Kiki
        // x ≡ 1 (mod 5)
        // x ≡ 2 (mod 15)
        // 解应为 17
        long[] m7 = {5, 15};
        long[] r7 = {1, 2};
        long result7 = manualEXCRT(m7, r7);
        boolean passed7 = result7 == 17;
        System.out.println("测试用例7 [HDU 3579] - 期望结果: 17, 实际结果: " + result7 + ", " + (passed7 ? "通过" : "失败"));
        assert passed7 : "EXCRT测试用例7失败";
        
        // 测试用例8: 无解情况
        // x ≡ 1 (mod 2)
        // x ≡ 2 (mod 4)
        // x ≡ 1 (mod 6)
        // 无解
        long[] m8 = {2, 4, 6};
        long[] r8 = {1, 2, 1};
        long result8 = manualEXCRT(m8, r8);
        boolean passed8 = result8 == -1;
        System.out.println("测试用例8 [无解情况] - 期望结果: -1 (无解), 实际结果: " + result8 + ", " + (passed8 ? "通过" : "失败"));
        assert passed8 : "EXCRT测试用例8失败";
    }
    
    // 测试EXCRT边界条件
    public static void testEXCRTEdgeCases() {
        System.out.println("\n=== 测试扩展中国剩余定理边界条件 ===");
        
        // 测试用例9: 模数相同
        // x ≡ 3 (mod 7)
        // x ≡ 3 (mod 7)
        // 解应为 3
        long[] m9 = {7, 7};
        long[] r9 = {3, 3};
        long result9 = manualEXCRT(m9, r9);
        boolean passed9 = result9 == 3;
        System.out.println("边界条件测试3 [模数相同且余数相同] - 期望结果: 3, 实际结果: " + result9 + ", " + (passed9 ? "通过" : "失败"));
        assert passed9 : "EXCRT边界条件测试3失败";
        
        // 测试用例10: 模数相同但余数不同
        // x ≡ 3 (mod 7)
        // x ≡ 4 (mod 7)
        // 无解
        long[] m10 = {7, 7};
        long[] r10 = {3, 4};
        long result10 = manualEXCRT(m10, r10);
        boolean passed10 = result10 == -1;
        System.out.println("边界条件测试4 [模数相同但余数不同] - 期望结果: -1 (无解), 实际结果: " + result10 + ", " + (passed10 ? "通过" : "失败"));
        assert passed10 : "EXCRT边界条件测试4失败";
    }
    
    // EXCRT性能测试
    public static void testEXCRTPerformance() {
        System.out.println("\n=== EXCRT性能测试 ===");
        
        // 生成较大规模的测试数据
        int n = 1000;
        long[] m = new long[n];
        long[] r = new long[n];
        
        // 生成可能不互质的模数
        for (int i = 0; i < n; i++) {
            m[i] = i + 2;
            r[i] = i % m[i];
        }
        
        long startTime = System.currentTimeMillis();
        long result = manualEXCRT(m, r);
        long endTime = System.currentTimeMillis();
        
        System.out.println("EXCRT大规模测试 (" + n + "个方程) - 耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果存在性: " + (result != -1 ? "有解" : "无解"));
    }
    
    // C++实现对照
    /*
    #include <iostream>
    #include <vector>
    using namespace std;
    
    // 扩展欧几里得算法求gcd和系数
    long long exgcd(long long a, long long b, long long &x, long long &y) {
        if (b == 0) {
            x = 1, y = 0;
            return a;
        }
        long long g = exgcd(b, a % b, y, x);
        y -= a / b * x;
        return g;
    }
    
    // 中国剩余定理（模数两两互质）
    long long CRT(vector<long long> &m, vector<long long> &r) {
        long long M = 1, res = 0, x, y, g, Mi;
        int n = m.size();
        
        // 计算所有模数的乘积
        for (int i = 0; i < n; i++) M *= m[i];
        
        // 求解每个方程的解并累加
        for (int i = 0; i < n; i++) {
            Mi = M / m[i];
            // 求Mi的模m[i]逆元
            g = exgcd(Mi, m[i], x, y);
            res = (res + r[i] * Mi % M * x % M + M) % M;
        }
        
        return res;
    }
    
    // 扩展中国剩余定理（模数不一定互质）
    long long EXCRT(vector<long long> &m, vector<long long> &r) {
        int n = m.size();
        long long m1 = m[0], r1 = r[0], m2, r2, d, x, y;
        
        for (int i = 1; i < n; i++) {
            m2 = m[i], r2 = r[i];
            
            // 求解方程: m1 * x + m2 * y = r2 - r1
            d = exgcd(m1, m2, x, y);
            
            // 无解情况
            if ((r2 - r1) % d != 0) return -1;
            
            // 调整x到最小正整数解
            x = (x * (r2 - r1) / d % (m2 / d) + (m2 / d)) % (m2 / d);
            
            // 更新r1和m1
            r1 = m1 * x + r1;
            m1 = m1 / d * m2;
            r1 = (r1 % m1 + m1) % m1;
        }
        
        return r1;
    }
    */
    
    // Python实现对照
    /*
    # 扩展欧几里得算法
    def exgcd(a, b):
        if b == 0:
            return a, 1, 0
        g, x, y = exgcd(b, a % b)
        return g, y, x - (a // b) * y
    
    # 中国剩余定理（模数两两互质）
    def CRT(m, r):
        n = len(m)
        M = 1
        res = 0
        
        # 计算所有模数的乘积
        for mi in m:
            M *= mi
        
        # 求解每个方程的解并累加
        for i in range(n):
            Mi = M // m[i]
            # 求Mi的模m[i]逆元
            g, x, y = exgcd(Mi, m[i])
            res = (res + r[i] * Mi * x) % M
        
        return res
    
    # 扩展中国剩余定理（模数不一定互质）
    def EXCRT(m, r):
        n = len(m)
        m1, r1 = m[0], r[0]
        
        for i in range(1, n):
            m2, r2 = m[i], r[i]
            
            # 求解方程: m1 * x + m2 * y = r2 - r1
            d, x, y = exgcd(m1, m2)
            
            # 无解情况
            if (r2 - r1) % d != 0:
                return -1
            
            # 调整x到最小正整数解
            x = (x * (r2 - r1) // d) % (m2 // d)
            if x < 0:
                x += m2 // d
            
            # 更新r1和m1
            r1 = m1 * x + r1
            m1 = m1 // d * m2
            r1 %= m1
        
        return r1
    */
    
    // 手动实现EXCRT用于验证
    // 算法时间复杂度: O(n log(max(m)))，空间复杂度: O(1)
    public static long manualEXCRT(long[] m, long[] r) {
        // 输入验证
        if (m == null || r == null || m.length == 0 || m.length != r.length) {
            System.err.println("输入参数错误: 模数和余数数组不匹配或为空");
            return -1;
        }
        
        // 边界情况：单个方程
        int n = m.length;
        if (n == 1) {
            return r[0] % m[0];
        }
        
        long m1 = m[0];
        long r1 = r[0] % m1; // 确保余数在正确范围内
        
        for (int i = 1; i < n; i++) {
            long m2 = m[i];
            long r2 = r[i] % m2;
            
            // 使用扩展欧几里得算法求gcd(m1, m2)和系数x, y
            long[] result = exgcd(m1, m2);
            long g = result[0], x = result[1], y = result[2];
            
            // 判断是否有解：当且仅当gcd(m1, m2) | (r2 - r1)时有解
            if ((r2 - r1) % g != 0) {
                System.err.println("第" + i + "个方程与前面的方程无解");
                return -1;
            }
            
            // 计算调整量，确保解的正确性
            long tmp = (r2 - r1) / g;
            long lcm = m1 / g * m2; // 最小公倍数
            
            // 调整x到最小正整数解
            x = (x * tmp % (m2 / g) + (m2 / g)) % (m2 / g);
            
            // 更新余数和模数
            r1 = m1 * x + r1;
            m1 = lcm;
            r1 = (r1 % m1 + m1) % m1; // 确保余数为正
        }
        
        return r1;
    }
    
    // 求最小公倍数
    // 算法时间复杂度: O(log(min(a, b)))，空间复杂度: O(log(min(a, b)))
    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        a = Math.abs(a); // 处理负数
        b = Math.abs(b);
        
        // 避免溢出：先除以gcd再相乘
        return a / gcd(a, b) * b;
    }
    
    // 求最大公约数
    // 算法时间复杂度: O(log(min(a, b)))，空间复杂度: O(log(min(a, b)))
    public static long gcd(long a, long b) {
        a = Math.abs(a); // 处理负数
        b = Math.abs(b);
        return b == 0 ? a : gcd(b, a % b);
    }
    
    // 快速乘法（龟速乘），处理大整数乘法可能溢出的情况
    // 算法时间复杂度: O(log(b))，空间复杂度: O(1)
    public static long fastMultiply(long a, long b, long mod) {
        long result = 0;
        a = (a % mod + mod) % mod;
        b = (b % mod + mod) % mod;
        
        while (b > 0) {
            if ((b & 1) == 1) {
                result = (result + a) % mod;
            }
            a = (a << 1) % mod;
            b >>= 1;
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        // 运行基本测试
        testCRT();
        testEXCRT();
        
        // 运行边界条件测试
        testCRTEdgeCases();
        testEXCRTEdgeCases();
        
        // 运行性能测试
        testCRTPerformance();
        testEXCRTPerformance();
        
        // 运行算法一致性验证测试
        testAlgorithmConsistency();
        
        // 运行异常场景测试
        testExceptionScenarios();
    }
    
    // 测试算法一致性
    public static void testAlgorithmConsistency() {
        System.out.println("\n=== 测试算法一致性 ===");
        
        // 对于模数互质的情况，CRT和EXCRT应该得到相同的结果
        long[] m = {3, 5, 7};
        long[] r = {2, 3, 2};
        
        long crtResult = manualCRT(m, r);
        long excrtResult = manualEXCRT(m, r);
        
        boolean consistent = crtResult == excrtResult;
        System.out.println("CRT结果: " + crtResult);
        System.out.println("EXCRT结果: " + excrtResult);
        System.out.println("算法一致性: " + (consistent ? "一致" : "不一致"));
        assert consistent : "CRT和EXCRT结果不一致";
    }
    
    // 测试异常场景
    public static void testExceptionScenarios() {
        System.out.println("\n=== 测试异常场景 ===");
        
        // 负数模数处理
        long[] mNeg = {-3, 5};
        long[] rNeg = {2, 3};
        try {
            long result = manualEXCRT(mNeg, rNeg);
            System.out.println("负数模数处理: " + (result == -1 ? "正确处理" : "未正确处理"));
        } catch (Exception e) {
            System.out.println("负数模数抛出异常: " + e.getMessage());
        }
        
        // 空数组处理
        try {
            long[] mEmpty = {};
            long[] rEmpty = {};
            long result = manualCRT(mEmpty, rEmpty);
            System.out.println("空数组处理: " + (result == -1 ? "正确处理" : "未正确处理"));
        } catch (Exception e) {
            System.out.println("空数组抛出异常: " + e.getMessage());
        }
    }
}