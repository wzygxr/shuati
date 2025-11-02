"""
洛谷 P5026 Lycanthropy

题目描述:
一个朋友落水后会对水面产生影响。当体积为v的朋友在位置x落水时，
会产生特定的水位分布。具体影响范围和数值计算方式在题目中有详细说明。

测试链接 : https://www.luogu.com.cn/problem/P5026

相关题目:
1. 洛谷 P5026 Lycanthropy
   链接: https://www.luogu.com.cn/problem/P5026
   题目描述: 一个朋友落水后会对水面产生影响。当体积为v的朋友在位置x落水时，
            会产生特定的水位分布。具体影响范围和数值计算方式在题目中有详细说明。

2. 洛谷 P4231 三步必杀
   链接: https://www.luogu.com.cn/problem/P4231
   题目描述: 在区间上添加等差数列，使用二阶差分处理。

3. Codeforces 296C Greg and Array
   链接: https://codeforces.com/contest/296/problem/C
   题目描述: 多层差分操作，需要处理操作的操作。

偏移量处理核心思想:
在某些差分问题中，操作可能会影响数组边界外的位置，为了简化处理，
我们引入偏移量OFFSET，将所有操作都映射到正索引范围内。

对于本题:
1. 每个人落水会产生4个等差数列影响区域
2. 为了防止负索引出现，使用OFFSET偏移量
3. 最终结果数组大小为 OFFSET + MAXN + OFFSET

时间复杂度分析:
每次落水操作: O(1)
构造结果数组(两次前缀和): O(m + OFFSET)
总时间复杂度: O(n + m + OFFSET)

空间复杂度分析:
需要额外的数组空间: O(OFFSET + MAXN + OFFSET)

这是最优解，因为:
1. 需要处理所有人落水的影响，无法避免O(n)的时间复杂度
2. 使用差分数组将区间更新操作从O(v)优化到O(1)
3. 最终需要计算所有位置的水位，需要O(m)时间
"""


def main():
    """
    主函数，处理输入并输出结果
    
    解题思路:
    1. 使用偏移量处理防止负索引出现
    2. 对于每个人落水，产生4个区域的水位变化
    3. 使用等差数列差分技巧处理每个区域
    4. 通过两次前缀和操作得到最终水位数组
    
    时间复杂度: O(n + m + OFFSET) - 需要处理所有落水操作和数组两次前缀和
    空间复杂度: O(OFFSET + MAXN + OFFSET) - 需要存储差分数组
    
    工程化考量:
    1. 输入处理: 使用高效的输入处理方式
    2. 边界处理: 使用偏移量避免负索引
    3. 性能优化: 使用差分数组避免重复计算
    4. 输出格式: 按照题目要求输出结果
    """
    try:
        while True:
            line = input().split()
            n = int(line[0])  # 落水人数
            m = int(line[1])  # 湖泊宽度
            
            # 题目说了m <= 10^6，代表湖泊宽度
            MAXN = 1000001
            
            # 数值保护，因为题目中v最大是10000
            # 所以左侧影响最远的位置到达了x - 3 * v + 1
            # 所以右侧影响最远的位置到达了x + 3 * v - 1
            # x如果是正式的位置(1~m)，那么左、右侧可能超过正式位置差不多30000的规模
            OFFSET = 30001
            
            # 湖泊宽度是MAXN，是正式位置的范围
            # 左、右侧可能超过正式位置差不多OFFSET的规模
            # 所以准备一个长度为OFFSET + MAXN + OFFSET的数组
            arr = [0] * (OFFSET + MAXN + OFFSET)
            
            # 处理每个人落水
            for _ in range(n):
                v, x = map(int, input().split())
                # v体积的朋友，在x处落水，修改差分数组
                fall(arr, v, x, OFFSET)
            
            # 生成最终的水位数组
            build(arr, m, OFFSET)
            
            # 开始收集答案
            # 0...OFFSET这些位置是辅助位置，为了防止越界设计的
            # 从OFFSET+1开始往下数m个，才是正式的位置1...m
            # 打印这些位置，就是返回正式位置1...m的水位
            start = OFFSET + 1
            result = []
            for i in range(m):
                result.append(str(arr[start + i]))
            
            print(" ".join(result))
            
    except EOFError:
        pass


def fall(arr, v, x, offset):
    """
    处理一个人落水的情况
    当体积为v的朋友在位置x落水时，会产生4个区域的水位变化
    
    Args:
        arr: 差分数组
        v: 朋友体积
        x: 落水位置
        offset: 偏移量
    """
    # 区域1: [x - 3*v + 1, x - 2*v] 水位依次增加 1, 2, ..., v
    set_diff(arr, x - 3 * v + 1, x - 2 * v, 1, v, 1, offset)
    # 区域2: [x - 2*v + 1, x] 水位依次减少 (v-1), (v-2), ..., 1, 0
    set_diff(arr, x - 2 * v + 1, x, v - 1, -v, -1, offset)
    # 区域3: [x + 1, x + 2*v] 水位依次减少 -1, -2, ..., -v
    set_diff(arr, x + 1, x + 2 * v, -v + 1, v, 1, offset)
    # 区域4: [x + 2*v + 1, x + 3*v - 1] 水位依次减少 (v-1), (v-2), ..., 1
    set_diff(arr, x + 2 * v + 1, x + 3 * v - 1, v - 1, 1, -1, offset)


def set_diff(arr, l, r, s, e, d, offset):
    """
    在区间[l,r]上加上首项为s、末项为e、公差为d的等差数列
    这是等差数列差分的核心操作
    
    Args:
        arr: 差分数组
        l: 区间起始位置
        r: 区间结束位置
        s: 首项
        e: 末项
        d: 公差
        offset: 偏移量
    """
    # 为了防止x - 3 * v + 1出现负数下标，进而有很多很烦的边界讨论
    # 所以任何位置，都加上一个较大的数字(offset)
    # 这样一来，所有下标就都在0以上了，省去了大量边界讨论
    arr[l + offset] += s
    arr[l + 1 + offset] += d - s
    arr[r + 1 + offset] -= d + e
    arr[r + 2 + offset] += e


def build(arr, m, offset):
    """
    通过两次前缀和操作构建最终结果数组
    
    Args:
        arr: 差分数组
        m: 湖泊宽度
        offset: 偏移量
    """
    # 第一次前缀和，得到一阶差分的结果
    for i in range(1, m + offset + 1):
        arr[i] += arr[i - 1]
    
    # 第二次前缀和，得到最终结果
    for i in range(1, m + offset + 1):
        arr[i] += arr[i - 1]


if __name__ == "__main__":
    main()