# 中国剩余定理(CRT)与扩展中国剩余定理(EXCRT)相关题目大全

## 已实现的题目

### CRT相关题目

1. **洛谷 P1495【模板】中国剩余定理（CRT）/ 曹冲养猪**
   - 链接：https://www.luogu.com.cn/problem/P1495
   - 题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
   - 解题思路：标准的中国剩余定理模板题，直接应用CRT公式求解
   - 难度：基础
   - Java实现：✓
   - Python实现：✓
   - C++实现：✓

2. **51Nod 1079 中国剩余定理**
   - 链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
   - 题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
   - 解题思路：题目保证所有模数都是质数，所以两两互质，直接应用CRT
   - 难度：基础
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

3. **POJ 1006 Biorhythms**
   - 链接：http://poj.org/problem?id=1006
   - 题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数
   - 解题思路：三个生理周期分别为23、28、33天，它们两两互质，可以直接应用中国剩余定理
   - 难度：基础
   - Java实现：✓
   - Python实现：✓
   - C++实现：✓

4. **UVA 756 Biorhythms**
   - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
   - 题目大意：与POJ 1006相同
   - 解题思路：标准的CRT应用题
   - 难度：基础
   - Java实现：在POJ1006中实现
   - Python实现：在POJ1006中实现
   - C++实现：在POJ1006中实现

5. **洛谷 P3868【TJOI2009】猜数字**
   - 链接：https://www.luogu.com.cn/problem/P3868
   - 题目大意：现在有两组数字，a1,a2,...,an 和 b1,b2,...,bn。其中，ai两两互质。现在请求出一个最小的正整数N，使得bi | (N - ai) 对所有i成立。
   - 解题思路：条件bi | (N - ai)等价于N ≡ ai (mod bi)，这就转化为了标准的中国剩余定理问题
   - 难度：中等
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

### EXCRT相关题目

1. **洛谷 P4777【模板】扩展中国剩余定理（EXCRT）**
   - 链接：https://www.luogu.com.cn/problem/P4777
   - 题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
   - 解题思路：标准的扩展中国剩余定理模板题，通过合并方程求解
   - 难度：基础
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

2. **POJ 2891 Strange Way to Express Integers**
   - 链接：http://poj.org/problem?id=2891
   - 题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
   - 解题思路：与洛谷P4777相同，是EXCRT的标准应用
   - 难度：中等
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

3. **NOI 2018 屠龙勇士**
   - 链接：https://www.luogu.com.cn/problem/P4774
   - 题目大意：游戏中需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]，勇士有m把剑，每把剑有攻击力，求最少攻击次数
   - 解题思路：将问题转化为线性同余方程组，然后用EXCRT求解
   - 难度：困难
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

4. **Codeforces 707D Two chandeliers**
   - 链接：https://codeforces.com/contest/1483/problem/D
   - 题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，求第k次生气在第几天
   - 解题思路：枚举颜色相同的配对，转化为同余方程组求解
   - 难度：困难
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

5. **UVa 11754 Code Feat**
   - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
   - 题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
   - 解题思路：枚举所有可能的余数组合，对每个组合使用EXCRT求解
   - 难度：困难
   - Java实现：✓
   - Python实现：✓
   - C++实现：部分

6. **HDU 3579 Hello Kiki**
   - 链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
   - 题目大意：求解同余方程组，模数不一定互质
   - 解题思路：标准的EXCRT应用题
   - 难度：中等
   - Java实现：在相关题目中提及
   - Python实现：在相关题目中提及
   - C++实现：在相关题目中提及

## 未实现但相关的题目

### LeetCode平台

1. **LeetCode 372. 超级次方**
   - 链接：https://leetcode.cn/problems/super-pow/
   - 题目大意：计算 a^b mod 1337，其中b是一个非常大的数，用数组表示
   - 解题思路：可以使用中国剩余定理：由于1337=7×191，且gcd(7,191)=1，我们可以分别计算x1 = a^b mod 7和x2 = a^b mod 191，然后使用中国剩余定理合并结果。
   - 难度：中等
   - Java实现：✓ (已实现)
   - Python实现：✓ (已实现)
   - C++实现：未实现

### Codeforces平台

1. **Codeforces 919E Chinese Remainder Theorem**
   - 链接：https://codeforces.com/contest/919/problem/E
   - 题目大意：涉及中国剩余定理的应用
   - 解题思路：使用CRT解决特定的数学问题
   - 难度：困难

2. **Codeforces 338D GCD Table**
   - 链接：https://codeforces.com/contest/338/problem/D
   - 题目大意：给定n*m的矩阵，[i,j]的点值为gcd(i,j)，给定一个k长的序列，问是否能匹配上矩阵的某一行的连续k个数字
   - 解题思路：使用中国剩余定理优化搜索
   - 难度：困难

### Project Euler平台

1. **Project Euler Problem 271 Modular Cubes, part 1**
   - 链接：https://projecteuler.net/problem=271
   - 题目大意：求解x^3 ≡ 1 (mod n)的解
   - 解题思路：使用中国剩余定理分解模数
   - 难度：中等

2. **Project Euler Problem 531 Chinese leftovers**
   - 链接：https://projecteuler.net/problem=531
   - 题目大意：涉及中国剩余定理的应用
   - 解题思路：处理非互质模数的情况
   - 难度：困难

### 其他平台

1. **AtCoder Beginner Contest 186 F. Rook on Grid**
   - 链接：https://atcoder.jp/contests/abc186/tasks/abc186_f
   - 解题思路：可使用EXCRT解决的周期性问题
   - 难度：中等

2. **SPOJ - MOD**
   - 链接：https://www.spoj.com/problems/MOD/
   - 题目大意：求解同余方程组，模数不一定互质
   - 解题思路：标准的EXCRT应用
   - 难度：中等

3. **SPOJ - CHIAVI**
   - 链接：https://www.spoj.com/problems/CHIAVI/
   - 题目大意：密码学相关问题，涉及同余方程求解
   - 解题思路：使用CRT/EXCRT解决密码学问题
   - 难度：困难

4. **牛客网 - NC15857 同余方程**
   - 链接：https://ac.nowcoder.com/acm/problem/15857
   - 题目大意：求解同余方程组，模数两两互质
   - 解题思路：标准的CRT应用
   - 难度：基础

5. **牛客网 - NC15293 同余方程**
   - 链接：https://ac.nowcoder.com/acm/problem/15293
   - 题目大意：求解同余方程组，模数不一定互质
   - 解题思路：标准的EXCRT应用
   - 难度：中等

6. **ZOJ - Chinese Remainder Theorem**
   - 链接：需要查找具体题目
   - 题目大意：标准的CRT/EXCRT应用题
   - 解题思路：根据模数是否互质选择相应算法
   - 难度：根据具体题目而定

7. **HackerRank - Number Theory**
   - 链接：https://www.hackerrank.com/domains/mathematics/number-theory
   - 题目大意：包含多个与CRT相关的题目
   - 解题思路：使用CRT/EXCRT解决数论问题
   - 难度：从基础到困难不等

8. **CodeChef - CHEFADV**
   - 链接：https://www.codechef.com/problems/CHEFADV
   - 题目大意：判断是否能在棋盘上移动，涉及同余条件
   - 解题思路：使用CRT解决同余条件判断问题
   - 难度：中等

9. **CodeChef - COPRIME3**
   - 链接：https://www.codechef.com/problems/COPRIME3
   - 题目大意：使用同余关系解决数论问题
   - 解题思路：结合CRT和数论知识
   - 难度：困难

10. **Timus OJ - Chinese Remainder Theorem Problems**
    - 链接：需要查找具体题目
    - 题目大意：涉及CRT的应用
    - 解题思路：根据具体题目选择相应算法
    - 难度：根据具体题目而定

## 题目分类总结

### 按难度分类

**基础题目：**
- 洛谷 P1495【模板】中国剩余定理
- 51Nod 1079 中国剩余定理
- POJ 1006 Biorhythms
- UVA 756 Biorhythms
- 洛谷 P4777【模板】扩展中国剩余定理

**中等题目：**
- 洛谷 P3868【TJOI2009】猜数字
- POJ 2891 Strange Way to Express Integers
- HDU 3579 Hello Kiki
- SPOJ - MOD
- 牛客网相关题目
- AtCoder相关题目
- CodeChef相关题目

**困难题目：**
- NOI 2018 屠龙勇士
- Codeforces 707D Two chandeliers
- UVa 11754 Code Feat
- Codeforces 919E
- Codeforces 338D
- Project Euler相关题目
- SPOJ - CHIAVI

### 按应用场景分类

**生物节律类：**
- POJ 1006 Biorhythms
- UVA 756 Biorhythms

**密码学应用类：**
- LeetCode 372. 超级次方
- SPOJ - CHIAVI
- Project Euler相关题目

**调度问题类：**
- NOI 2018 屠龙勇士
- Codeforces 707D Two chandeliers
- UVa 11754 Code Feat

**数论问题类：**
- 洛谷 P3868【TJOI2009】猜数字
- Codeforces 338D GCD Table
- CodeChef相关题目

**游戏问题类：**
- AtCoder相关题目
- Codeforces相关题目

## 学习建议

1. **初学者路径：**
   - 先掌握基础的CRT概念和实现
   - 从洛谷P1495和51Nod 1079开始练习
   - 理解生物节律类问题的应用

2. **进阶学习路径：**
   - 学习EXCRT的原理和实现
   - 练习洛谷P4777和POJ2891
   - 尝试解决调度问题类题目

3. **高级应用路径：**
   - 研究复杂的应用场景，如密码学和游戏问题
   - 挑战NOI和Codeforces的困难题目
   - 探索Project Euler中的数论问题

## 算法要点总结

**CRT适用条件：**
- 模数两两互质
- 可以直接使用公式求解
- 时间复杂度较低

**EXCRT适用条件：**
- 模数不一定互质
- 需要逐步合并方程
- 时间复杂度稍高但应用范围更广

**实现要点：**
- 熟练掌握扩展欧几里得算法
- 注意大数运算和溢出处理
- 合理使用龟速乘法优化
- 正确处理无解情况