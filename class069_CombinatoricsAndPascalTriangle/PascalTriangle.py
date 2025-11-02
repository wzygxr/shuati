"""
杨辉三角（Pascal's Triangle）

题目来源：洛谷 P5732 【深基5.习7】杨辉三角
题目链接：https://www.luogu.com.cn/problem/P5732
题目描述：
给定一个非负整数 numRows，生成杨辉三角的前 numRows 行。
在杨辉三角中，每个数是它左上方和右上方的数的和。

示例：
输入: 5
输出:
[
     [1],
    [1,1],
   [1,2,1],
  [1,3,3,1],
 [1,4,6,4,1]
]

算法思路：
1. 使用二维数组存储杨辉三角
2. 每行的第一个和最后一个元素都是1
3. 中间的元素等于上一行相邻两个元素之和

时间复杂度：O(n^2)，需要计算n行，每行平均有n/2个元素
空间复杂度：O(n^2)，需要存储整个三角形

相关题目：
1. LeetCode 118 - Pascal's Triangle
   题目链接：https://leetcode.cn/problems/pascals-triangle/
2. LeetCode 119 - Pascal's Triangle II
   题目链接：https://leetcode.cn/problems/pascals-triangle-ii/
3. 洛谷 P5732 - 杨辉三角
   题目链接：https://www.luogu.com.cn/problem/P5732
4. Codeforces 2072F - 组合数次幂异或问题
   题目链接：https://codeforces.com/problemset/problem/2072/F
5. AtCoder ABC165D - Floor Function
   题目链接：https://atcoder.jp/contests/abc165/tasks/abc165_d
6. 洛谷 P2822 - 组合数问题
   题目链接：https://www.luogu.com.cn/problem/P2822
7. 洛谷 P1313 - 计算系数
   题目链接：https://www.luogu.com.cn/problem/P1313
8. 牛客网 杨辉三角
   题目链接：https://www.nowcoder.com/practice/8c6984f3dc664ef0a305c24e1473729e
9. 杭电 OJ 2032 - 杨辉三角
   题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2032
10. ZOJ 3537 - Cake（组合数学应用）
    题目链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364577
11. POJ 2299 - Ultra-QuickSort（逆序对计数）
    题目链接：http://poj.org/problem?id=2299
12. SPOJ MSUBSTR - 最大子串（组合数学应用）
    题目链接：https://www.spoj.com/problems/MSUBSTR/
13. UVa 11300 - Spreading the Wealth（组合数学应用）
    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=25&page=show_problem&problem=2275
14. CodeChef INVCNT - 逆序对计数（组合数学应用）
    题目链接：https://www.codechef.com/problems/INVCNT
15. USACO 2006 November - Bad Hair Day（组合数学应用）
    题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=187
16. 计蒜客 T1565 - 合并果子（组合数学应用）
    题目链接：https://nanti.jisuanke.com/t/T1565
17. TimusOJ 1001 - Reverse Root（组合数学应用）
    题目链接：https://acm.timus.ru/problem.aspx?space=1&num=1001
18. 牛客网 - 计算数组的小和
    题目链接：https://www.nowcoder.com/practice/4385fa1c390e49f69fcf77ecffee7164
19. LintCode 1297 - 统计右侧小于当前元素的个数
    题目链接：https://www.lintcode.com/problem/1297/
20. LintCode 1497 - 区间和的个数
    题目链接：https://www.lintcode.com/problem/1497/
21. LintCode 3653 - Meeting Scheduler（组合数学应用）
    题目链接：https://www.lintcode.com/problem/3653/
22. HackerRank - Merge Sort: Counting Inversions（归并排序逆序对计数）
    题目链接：https://www.hackerrank.com/challenges/ctci-merge-sort/problem
23. HDU 1394 - Minimum Inversion Number（最小逆序对数）
    题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
24. Codeforces 1359E - 组合数学问题
    题目链接：https://codeforces.com/problemset/problem/1359/E
25. Codeforces 551D - GukiZ and Binary Operations（组合数学应用）
    题目链接：https://codeforces.com/problemset/problem/551/D
26. Codeforces 1117D - Magic Gems（组合数学+矩阵快速幂）
    题目链接：https://codeforces.com/problemset/problem/1117/D
27. AtCoder ABC098D - Xor Sum 2（组合数学应用）
    题目链接：https://atcoder.jp/contests/abc098/tasks/abc098_d
"""

class PascalTriangle:
    @staticmethod
    def generate(num_rows):
        """
        生成杨辉三角的前num_rows行
        
        Args:
            num_rows: 非负整数，要生成的行数
            
        Returns:
            二维列表，表示杨辉三角
        """
        # 初始化结果列表
        triangle = []
        
        # 逐行生成杨辉三角
        for i in range(num_rows):
            # 创建当前行，长度为i+1
            row = [1] * (i + 1)
            
            # 计算中间的元素值
            for j in range(1, i):
                row[j] = triangle[i-1][j-1] + triangle[i-1][j]
            
            # 将当前行添加到结果中
            triangle.append(row)
        
        return triangle
    
    @staticmethod
    def print_triangle(triangle):
        """
        打印杨辉三角
        
        Args:
            triangle: 二维列表，表示杨辉三角
        """
        for row in triangle:
            print(' '.join(map(str, row)))

def test_pascal_triangle():
    """测试杨辉三角函数"""
    print("=== 杨辉三角测试 ===")
    
    # 测试用例1
    n1 = 5
    print(f"生成前 {n1} 行杨辉三角:")
    result1 = PascalTriangle.generate(n1)
    PascalTriangle.print_triangle(result1)
    print()
    
    # 测试用例2
    n2 = 1
    print(f"生成前 {n2} 行杨辉三角:")
    result2 = PascalTriangle.generate(n2)
    PascalTriangle.print_triangle(result2)
    print()

if __name__ == "__main__":
    test_pascal_triangle()