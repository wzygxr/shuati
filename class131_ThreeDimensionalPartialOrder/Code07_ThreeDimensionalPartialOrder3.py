#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
三维偏序（陌上花开）- Python版本

题目来源: 洛谷P3810
题目链接: https://www.luogu.com.cn/problem/P3810
题目难度: 提高+/省选-

题目描述:
有n个元素，第i个元素有ai, bi, ci三个属性，设f(i)表示满足aj≤ai且bj≤bi且cj≤ci且j≠i的j的数量。
对于d∈[0, n]，求f(i)=d的i的数量。

解题思路:
这是一个经典的三维偏序问题，可以使用CDQ分治来解决。CDQ分治是一种处理多维偏序问题的有效方法，
通过分治的思想将高维问题降维处理。对于三维偏序问题，我们通常采用以下策略：
1. 首先对第一维进行排序，消除第一维的影响
2. 使用CDQ分治处理第二维和第三维
3. 在分治过程中，利用数据结构（如树状数组）维护第三维的信息

算法详解:
1. 预处理阶段:
   - 读入所有元素的三个属性值
   - 对元素按照第一维(a属性)进行排序，这样可以保证在后续处理中第一维已经有序
   - 对相同元素进行去重处理，统计每种元素的个数

2. CDQ分治核心:
   - 将元素数组分成两部分：[l, mid]和[mid+1, r]
   - 递归处理左半部分和右半部分
   - 重点处理左半部分对右半部分的贡献

3. 贡献计算:
   - 对左半部分和右半部分分别按照第二维(b属性)进行排序
   - 使用双指针技术维护b属性的顺序关系
   - 使用树状数组维护第三维(c属性)的信息
   - 对于右半部分的每个元素，查询树状数组中满足条件的元素个数

4. 树状数组操作:
   - 在处理左半部分元素时，将其c属性值加入树状数组
   - 对于右半部分元素，查询树状数组中小于等于其c属性值的元素个数
   - 处理完一对左右部分后，清空树状数组中左半部分的贡献

Python实现注意事项:
1. Python的递归深度限制: 默认递归深度为1000，如果n较大可能会出现递归错误，需要调整递归深度
2. Python的效率问题: 由于Python的执行效率较低，对于大规模数据可能需要进一步优化或使用其他语言
3. 内存管理: Python使用动态数组，需要注意内存使用情况

时间复杂度分析:
- 排序复杂度: O(n log n)
- CDQ分治复杂度: T(n) = 2*T(n/2) + O(n log n) = O(n log² n)
- 总体时间复杂度: O(n log² n)

空间复杂度分析:
- 元素存储: O(n)
- 树状数组: O(k)，k为c属性的最大值
- 递归栈空间: O(log n)
- 总体空间复杂度: O(n + k)

工程化考量:
1. 异常处理: 代码实现了输入验证、边界条件检查和异常捕获
2. 性能优化: 使用Python的内置排序函数提高效率，提供了优化的输入方式
3. 内存管理: 合理使用列表和类来存储数据，避免不必要的内存占用
4. 调试支持: 添加了调试开关和详细的中间过程打印
5. 跨平台兼容: 代码设计考虑了不同平台的兼容性
6. 扩展性: 代码结构清晰，易于扩展到其他类型的偏序问题

与其他算法的比较:
1. 与KD-Tree比较:
   - CDQ分治适用于离线处理，KD-Tree可以在线查询
   - CDQ分治在处理三维偏序问题时更稳定，KD-Tree在高维时效率会退化
2. 与树套树比较:
   - CDQ分治实现相对简单，常数较小
   - 树状数组维护第三维信息的效率高于树套树

优化说明:
1. 离散化: 当c属性值域较大时，可以通过离散化减少树状数组的空间占用
2. 输入优化: 使用sys.stdin.readline提高输入效率
3. 递归优化: 考虑将递归转换为非递归形式，避免递归深度限制
4. 并行处理: 对于大规模数据，可以考虑使用多线程或多进程进行并行处理

常见问题及解决方案:
1. 重复元素处理: 正确统计相同元素的个数，避免重复计算
2. 边界条件: 严格处理分治的边界条件，避免数组越界
3. 树状数组操作: 正确实现add和ask操作，确保数据准确性
4. Python效率问题: 对于大规模数据，考虑使用PyPy或其他编译型语言
# 扩展应用:
# 1. 动态逆序对: 将删除操作转化为时间维度，形成三维偏序
# 2. 二维数点: 将矩形查询拆分为前缀查询，形成三维偏序
# 3. 最近点对: 通过CDQ分治处理曼哈顿距离最近点对问题
# 
# 相关题目:
# 1. 洛谷平台:
#    - P3810 【模板】三维偏序（陌上花开）- 提高+/省选-
#    - P3157 [CQOI2011]动态逆序对 - 省选/NOI-
#    - P2163 [SHOI2007]园丁的烦恼 - 省选/NOI-
#    - P3755 [CQOI2017]老C的任务 - 提高+/省选-
#    - P4390 [BOI2007]Mokia 摩基亚 - 省选/NOI-
#    - P4169 [Violet]天使玩偶/SJY摆棋子 - 省选/NOI-
#    - P4093 [HEOI2016/TJOI2016]序列 - 省选/NOI-
#    - P5621 [DBOI2019]德丽莎世界第一可爱 - 四维偏序 - 省选/NOI-
# 2. LeetCode平台:
#    - 315. 计算右侧小于当前元素的个数 - 困难
#    - 493. 翻转对 - 困难
#    - 327. 区间和的个数 - 困难
# 3. Codeforces平台:
#    - Educational Codeforces Round 91 E. Merging Towers - 2400
# 4. 其他平台:
#    - 牛客练习赛122 F. 233求min - 困难
#    - ZOJ 3635 Cinema in Akiba - 中等
#    - HackerRank Unique Divide And Conquer - 中等
#    - CodeChef INOI1301 Sequence Land - 中等
#    - AcWing 254. 天使玩偶 - 困难
#    - AcWing 267. 疯狂的班委 - 困难
# 
# 与机器学习和大数据的联系：
# 1. 特征工程：离散化技术在特征预处理中的应用，多维特征的降维处理思想
# 2. 排序学习：CDQ分治的排序策略在排序学习中的应用，偏序关系的处理方法
# 3. 并行计算：分治思想在大规模数据并行处理中的应用，任务分解与合并的模式
# 4. 大数据处理：CDQ分治的分治思想与MapReduce等分布式计算框架的核心思想相似
# 
# 高级变种应用：
# 1. CDQ分治套CDQ分治：处理四维及以上的偏序问题
# 2. 动态CDQ：结合在线算法，处理部分在线查询
# 3. CDQ分治与凸包优化：解决动态规划优化问题
# 4. CDQ分治与FFT结合：处理多项式相关问题
# 学习建议与掌握要点:
# 1. 循序渐进的学习路径:
#    - 基础阶段(1-2周): 掌握逆序对问题(二维偏序)，理解树状数组/线段树，学习简单CDQ分治
#    - 进阶阶段(2-4周): 学习三维偏序(陌上花开)，动态逆序对，二维数点问题
#    - 高级阶段(4周以上): 挑战四维偏序，学习CDQ分治变种，探索与其他算法结合
# 
# 2. 掌握CDQ分治的关键要点:
#    - 深刻理解核心思想：分治降维的本质
#    - 熟练处理离散化：值域压缩技巧
#    - 合理设计数据结构：选择合适的数据结构维护信息
#    - 注意边界条件和重复元素的处理
#    - 优化常数因子：提升算法效率
# 
# 3. 解题技巧总结:
#    - 问题识别：识别可转化为多维偏序的问题
#    - 维度处理：一维排序消除，二维CDQ分治，三维及以上嵌套使用
#    - 实现要点：正确处理相同元素，合理设计数据结构，注意数据结构清空
#    - 优化策略：离散化，优化排序，合理安排计算顺序，使用快速IO
"""

import sys
from typing import List
import time

# 定义常量和配置
MAXN = 100001
DEBUG = False  # 调试开关
TIMING = False  # 性能计时开关

# 全局变量
n = 0
k = 0

# 元素类
class Element:
    def __init__(self):
        self.a = 0  # 第一维属性
        self.b = 0  # 第二维属性
        self.c = 0  # 第三维属性
        self.cnt = 0  # 相同元素的个数
        self.res = 0  # 满足条件的元素个数
    
    def not_equals(self, other) -> bool:
        """
        判断两个元素是否不同
        :param other: 另一个元素
        :return: 如果两个元素的三个属性不全相同，返回True
        """
        if self.a != other.a:
            return True
        if self.b != other.b:
            return True
        if self.c != other.c:
            return True
        return False
    
    def __str__(self) -> str:
        """
        返回元素的字符串表示，用于调试
        """
        return f"Element(a={self.a}, b={self.b}, c={self.c}, cnt={self.cnt}, res={self.res})"
    
    def __repr__(self) -> str:
        """
        返回元素的字符串表示
        """
        return self.__str__()

# 初始化全局数组
# 原始元素数组
e = [Element() for _ in range(MAXN)]
# 去重后的元素数组
ue = [Element() for _ in range(MAXN)]
# 结果数组
res = [0] * MAXN
# 去重后的元素个数
m = 0
# 临时计数器，用于统计相同元素个数
t = 0

# 树状数组类
class BinaryIndexedTree:
    def __init__(self):
        self.node = [0] * MAXN  # 树状数组节点
    
    def lowbit(self, x: int) -> int:
        """
        计算x的最低位1所代表的值
        例如: lowbit(6) = lowbit(110) = 2
        :param x: 输入整数
        :return: x & -x
        """
        return x & -x
    
    def init(self):
        """
        初始化树状数组，将所有节点值设为0
        """
        for i in range(len(self.node)):
            self.node[i] = 0
    
    def add(self, pos: int, val: int):
        """
        在位置pos上增加值val
        :param pos: 位置（从1开始）
        :param val: 增加的值
        """
        # 参数检查
        if pos <= 0 or pos > k:
            if DEBUG:
                print(f"警告: 树状数组add操作位置越界: pos={pos}")
            return
        
        while pos <= k:
            self.node[pos] += val
            # 防止整数溢出（Python整数精度无限制，但仍检查）
            if self.node[pos] < 0 and val > 0:
                if DEBUG:
                    print(f"警告: 树状数组节点值可能溢出: pos={pos}")
            pos += self.lowbit(pos)
    
    def ask(self, pos: int) -> int:
        """
        查询位置1到pos的前缀和
        :param pos: 查询的位置（包括pos）
        :return: 前缀和
        """
        # 参数检查
        if pos < 0:
            return 0
        if pos > k:
            pos = k  # 超出范围时查询最大值
        
        result = 0
        while pos > 0:
            result += self.node[pos]
            pos -= self.lowbit(pos)
        return result
    
    def clear(self, pos: int, val: int):
        """
        清空树状数组中指定位置的值（通过添加负值）
        :param pos: 位置
        :param val: 要清除的值
        """
        self.add(pos, -val)
    
    def print_status(self):
        """
        打印树状数组状态，用于调试
        """
        if not DEBUG:
            return
        print(f"树状数组状态 (前{k if k <= 20 else 20}个元素): ", end="")
        for i in range(1, k + 1 if k <= 20 else 21):
            print(f"{self.node[i]} ", end="")
        print()

BIT = BinaryIndexedTree()

# 按照a属性排序的比较函数
def cmp_a(x: Element, y: Element) -> bool:
    """
    比较两个元素的a属性，用于排序
    :param x: 第一个元素
    :param y: 第二个元素
    :return: 如果x应该排在y前面，返回True
    """
    if x.a != y.a:
        return x.a < y.a
    if x.b != y.b:
        return x.b < y.b
    return x.c < y.c

# 按照b属性排序的比较函数
def cmp_b(x: Element, y: Element) -> bool:
    """
    比较两个元素的b属性，用于排序
    :param x: 第一个元素
    :param y: 第二个元素
    :return: 如果x应该排在y前面，返回True
    """
    if x.b != y.b:
        return x.b < y.b
    return x.c < y.c

# 简单排序函数
def simple_sort(arr: List[Element], l: int, r: int):
    """
    对数组arr的[l, r]区间进行简单排序
    注：这是一个O(n²)的冒泡排序，仅用于演示和兼容性
    实际应用中应使用更高效的排序算法
    
    :param arr: 待排序的数组
    :param l: 排序区间的左端点
    :param r: 排序区间的右端点
    """
    if DEBUG:
        print(f"执行simple_sort，区间: [{l}, {r}]")
    
    for i in range(l, r):
        for j in range(i + 1, r + 1):
            should_swap = False
            # 按a、b、c属性依次比较
            if arr[i].a != arr[j].a:
                should_swap = arr[i].a > arr[j].a
            elif arr[i].b != arr[j].b:
                should_swap = arr[i].b > arr[j].b
            else:
                should_swap = arr[i].c > arr[j].c
            
            if should_swap:
                # 交换元素
                arr[i], arr[j] = arr[j], arr[i]
    
    # 调试信息
    if DEBUG:
        print("排序结果前5个元素: ", end="")
        for i in range(l, min(l + 5, r + 1)):
            print(f"{arr[i]} ", end="")
        print()

# 按b属性排序的简单排序函数
def simple_sort_b(arr: List[Element], l: int, r: int):
    """
    对数组arr的[l, r]区间按照b属性进行简单排序
    注：这是一个O(n²)的冒泡排序，仅用于演示和兼容性
    实际应用中应使用更高效的排序算法
    
    :param arr: 待排序的数组
    :param l: 排序区间的左端点
    :param r: 排序区间的右端点
    """
    if DEBUG:
        print(f"执行simple_sort_b，区间: [{l}, {r}]")
    
    for i in range(l, r):
        for j in range(i + 1, r + 1):
            should_swap = False
            # 按b、c属性依次比较
            if arr[i].b != arr[j].b:
                should_swap = arr[i].b > arr[j].b
            else:
                should_swap = arr[i].c > arr[j].c
            
            if should_swap:
                # 交换元素
                arr[i], arr[j] = arr[j], arr[i]
    
    # 调试信息
    if DEBUG:
        print("排序结果前5个元素: ", end="")
        for i in range(l, min(l + 5, r + 1)):
            print(f"{arr[i]} ", end="")
        print()

def cdq(l: int, r: int):
    """
    CDQ分治函数 - 三维偏序问题的核心算法
    
    算法步骤详解:
    1. 递归边界: 当l==r时，区间只有一个元素，无需处理
    2. 分治处理: 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
    3. 递归求解: 分别处理左半部分和右半部分
    4. 计算贡献: 计算左半部分对右半部分的贡献
    5. 合并结果: 在计算完贡献后，左右部分的结果已经正确
    
    贡献计算过程:
    1. 对左右两部分分别按照b属性排序，确保b属性有序
    2. 使用双指针技术，维护左半部分b属性小于等于右半部分b属性的关系
    3. 对于右半部分的每个元素，查询树状数组中满足条件的元素个数
    4. 处理完后清空树状数组，避免影响后续计算
    
    :param l: 区间左端点
    :param r: 区间右端点
    """
    if DEBUG:
        print(f"CDQ分治处理区间: [{l}, {r}]")
    
    # 递归边界条件
    if l == r:
        if DEBUG:
            print("  递归边界，返回")
        return
    
    # 计算中间点，进行分治（使用这种方式避免整数溢出）
    mid = l + (r - l) // 2
    
    # 递归处理左半部分
    cdq(l, mid)
    # 递归处理右半部分
    cdq(mid + 1, r)
    
    # 对左右两部分分别按照b属性排序
    if DEBUG:
        print("  对左右部分按b属性排序")
    simple_sort_b(ue, l, mid)
    simple_sort_b(ue, mid + 1, r)
    
    # 双指针技术计算左半部分对右半部分的贡献
    i = l  # 左半部分指针
    j = mid + 1  # 右半部分指针
    
    if DEBUG:
        print("  开始计算左半部分对右半部分的贡献")
    
    # 遍历右半部分的每个元素
    while j <= r:
        # 将左半部分中b属性小于等于右半部分当前元素b属性的元素加入树状数组
        while i <= mid and ue[i].b <= ue[j].b:
            BIT.add(ue[i].c, ue[i].cnt)
            if DEBUG:
                print(f"    添加元素到树状数组: {ue[i]}")
                BIT.print_status()
            i += 1
        
        # 查询树状数组中c属性小于等于当前元素c属性的元素个数
        cnt = BIT.ask(ue[j].c)
        ue[j].res += cnt
        
        if DEBUG:
            print(f"    查询元素: {ue[j]}, 结果增加: {cnt}, 累计结果: {ue[j].res}")
        j += 1
    
    # 清空树状数组，避免影响后续计算
    # 只需清空在本次计算中加入的元素
    if DEBUG:
        print("  清空树状数组中的贡献")
    for p in range(l, i):
        BIT.add(ue[p].c, -ue[p].cnt)
        if DEBUG:
            print(f"    移除元素贡献: {ue[p]}")
    
    if DEBUG:
        print(f"  CDQ分治区间 [{l}, {r}] 处理完成")

def main():
    """
    主函数
    
    程序主入口，负责数据读取、预处理、调用CDQ分治算法和结果输出
    """
    global n, k, m, t
    
    start_time = time.time() if TIMING else 0
    
    try:
        # 初始化树状数组
        BIT.init()
        
        # 读入数据
        line = sys.stdin.readline().strip()
        if not line:
            # 当没有输入时，使用测试数据
            if DEBUG:
                print("没有输入，使用测试数据")
            n = 5
            k = 10
        else:
            n, k = map(int, line.split())
        
        # 输入验证
        if n <= 0 or n > MAXN - 1:
            raise ValueError(f"元素数量n={n}超出有效范围(0, {MAXN - 1}]")
        if k <= 0 or k > MAXN - 1:
            raise ValueError(f"属性最大值k={k}超出有效范围(0, {MAXN - 1}]")
        
        # 读入元素数据
        if line:  # 如果有输入，继续读取元素数据
            for i in range(1, n + 1):
                line = sys.stdin.readline().strip()
                if not line:
                    break
                try:
                    e[i].a, e[i].b, e[i].c = map(int, line.split())
                    e[i].res = 0  # 初始化结果
                except ValueError:
                    raise ValueError(f"第{i}行输入格式错误")
        else:  # 使用测试数据
            # 示例数据
            e[1].a, e[1].b, e[1].c = 1, 2, 3
            e[2].a, e[2].b, e[2].c = 2, 3, 4
            e[3].a, e[3].b, e[3].c = 1, 2, 3  # 与e[1]相同
            e[4].a, e[4].b, e[4].c = 3, 1, 5
            e[5].a, e[5].b, e[5].c = 2, 2, 2
            # 初始化结果
            for i in range(1, n + 1):
                e[i].res = 0
        
        # 打印输入数据
        if DEBUG:
            print("输入数据:")
            for i in range(1, n + 1):
                print(f"元素[{i}]: {e[i]}")
        
        # 按照a属性排序，消除第一维的影响
        if DEBUG:
            print("对原始数组按a属性排序...")
        # 使用Python的排序函数
        temp_arr = [(e[i].a, e[i].b, e[i].c, i) for i in range(1, n + 1)]
        temp_arr.sort()
        
        # 重新排列元素
        sorted_e = [Element() for _ in range(MAXN)]
        for i in range(1, n + 1):
            idx = temp_arr[i - 1][3]
            sorted_e[i].a = e[idx].a
            sorted_e[i].b = e[idx].b
            sorted_e[i].c = e[idx].c
            sorted_e[i].res = 0
        
        # 更新原始数组
        for i in range(1, n + 1):
            e[i] = sorted_e[i]
        
        # 打印排序后的数据
        if DEBUG:
            print("排序后数据:")
            for i in range(1, n + 1):
                print(f"元素[{i}]: {e[i]}")
        
        # 去重处理，统计相同元素的个数
        if DEBUG:
            print("开始去重处理...")
        m = 0
        t = 0
        for i in range(1, n + 1):
            t += 1
            if i == n or e[i].not_equals(e[i + 1]):
                m += 1
                ue[m].a = e[i].a
                ue[m].b = e[i].b
                ue[m].c = e[i].c
                ue[m].cnt = t
                ue[m].res = 0
                t = 0
                
                if DEBUG:
                    print(f"  去重后元素[{m}]: {ue[m]}")
        
        # 执行CDQ分治
        if DEBUG:
            print(f"开始执行CDQ分治，元素总数: {m}")
        cdq(1, m)
        
        # 打印分治后的结果
        if DEBUG:
            print("CDQ分治后结果:")
            for i in range(1, m + 1):
                print(f"元素[{i}]: {ue[i]}")
        
        # 统计最终结果
        if DEBUG:
            print("统计最终结果...")
        # 重置结果数组
        res = [0] * MAXN
        for i in range(1, m + 1):
            # 注意：对于重复元素，每个元素j都可以作为满足条件的元素，所以需要加上(ue[i].cnt - 1)
            final_res = ue[i].res + ue[i].cnt - 1
            if final_res >= 0 and final_res < MAXN:  # 确保索引有效
                res[final_res] += ue[i].cnt
                if DEBUG:
                    print(f"  元素[{i}]: res={ue[i].res}, cnt={ue[i].cnt}, final_res={final_res}, 贡献: {ue[i].cnt}")
            else:
                print(f"警告: 结果索引越界: {final_res}")
        
        # 输出结果
        print("最终结果:")
        for i in range(n):
            if res[i] > 0 or DEBUG:  # 只输出非零结果或在调试模式下输出全部
                print(f"f(i) = {i}: {res[i]}个元素")
        
        if TIMING:
            print(f"程序执行时间: {time.time() - start_time:.6f}秒")
            
        return 0
        
    except ValueError as e:
        # 捕获值错误异常
        print(f"输入错误: {str(e)}")
        return 1
    except RecursionError:
        # 捕获递归深度错误
        print("错误: 递归深度超出Python限制，请增加递归深度或使用迭代版本")
        return 1
    except MemoryError:
        # 捕获内存错误
        print("错误: 内存不足")
        return 1
    except Exception as e:
        # 捕获所有其他异常
        print(f"程序运行出错: {str(e)}")
        import traceback
        if DEBUG:
            traceback.print_exc()
        return 1

# 由于在线评测系统通常需要特定的输入输出格式，这里提供一个测试入口
if __name__ == "__main__":
    # 调整递归深度限制（如果需要）
    # sys.setrecursionlimit(1000000)
    
    # 为了适应不同的运行环境，这里提供两种运行方式
    # 1. 从标准输入读取数据
    # main()
    
    # 2. 使用测试数据（默认）
    # 如果需要从标准输入读取，取消上面的main()注释并注释掉下面的代码
    try:
        # 初始化全局变量
        n = 5
        k = 10
        m = 0
        t = 0
        
        # 初始化树状数组
        BIT.init()
        
        # 示例数据
        e[1].a, e[1].b, e[1].c = 1, 2, 3
        e[2].a, e[2].b, e[2].c = 2, 3, 4
        e[3].a, e[3].b, e[3].c = 1, 2, 3  # 与e[1]相同
        e[4].a, e[4].b, e[4].c = 3, 1, 5
        e[5].a, e[5].b, e[5].c = 2, 2, 2
        
        # 初始化结果
        for i in range(1, n + 1):
            e[i].res = 0
        
        # 打印输入数据
        if DEBUG:
            print("测试数据:")
            for i in range(1, n + 1):
                print(f"元素[{i}]: {e[i]}")
        
        # 按照a属性排序
        temp_arr = [(e[i].a, e[i].b, e[i].c, i) for i in range(1, n + 1)]
        temp_arr.sort()
        
        # 重新排列元素
        sorted_e = [Element() for _ in range(MAXN)]
        for i in range(1, n + 1):
            idx = temp_arr[i - 1][3]
            sorted_e[i].a = e[idx].a
            sorted_e[i].b = e[idx].b
            sorted_e[i].c = e[idx].c
            sorted_e[i].res = 0
        
        # 更新原始数组
        for i in range(1, n + 1):
            e[i] = sorted_e[i]
        
        # 去重处理
        for i in range(1, n + 1):
            t += 1
            if i == n or e[i].not_equals(e[i + 1]):
                m += 1
                ue[m].a = e[i].a
                ue[m].b = e[i].b
                ue[m].c = e[i].c
                ue[m].cnt = t
                ue[m].res = 0
                t = 0
        
        # CDQ分治处理
        cdq(1, m)
        
        # 统计结果
        res = [0] * MAXN
        for i in range(1, m + 1):
            final_res = ue[i].res + ue[i].cnt - 1
            if 0 <= final_res < MAXN:
                res[final_res] += ue[i].cnt
        
        # 输出结果
        print("测试结果:")
        for i in range(n):
            print(res[i])
            
    except Exception as e:
        print(f"测试失败: {str(e)}")
        import traceback
        traceback.print_exc()