# 三维博弈 (3D Nim Game)
# 一个三维空间里全是灯，每次选出一个正方体，改变八个角灯的状态
# 而且右下角的灯初始必须是开的
# 
# 题目来源：
# 1. POJ 3533 Light Switching Game - http://poj.org/problem?id=3533
# 2. HDU 3404 Nim积 - http://acm.hdu.edu.cn/showproblem.php?pid=3404
# 3. POJ 2975 Nim - http://poj.org/problem?id=2975
# 
# 算法核心思想：
# 1. 三维Nim积：利用Nim积计算三维空间中每个点的SG值
# 2. Nim积性质：(a⊗b)⊗c = a⊗(b⊗c)，a⊗b = b⊗a
# 3. SG函数：整个游戏的SG值为所有开灯位置SG值的异或和
# 
# 时间复杂度分析：
# 1. 预处理：O(x*y*z) - 计算每个位置的Nim积
# 2. 查询：O(k) - k为开灯数，计算所有开灯位置SG值的异或和
# 
# 空间复杂度分析：
# 1. Nim积数组：O(x*y*z) - 存储每个位置的Nim积
# 
# 工程化考量：
# 1. 异常处理：处理负数输入和边界情况
# 2. 性能优化：预处理Nim积避免重复计算
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的空间大小和查询

# 最大坐标值
MAXN = 21

# Nim积数组，nim[i][j]表示i和j的Nim积
nim = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

# SG数组，sg[x][y][z]表示位置(x,y,z)的SG值
sg = [[[0 for _ in range(MAXN)] for _ in range(MAXN)] for _ in range(MAXN)]

def build():
    """
    算法原理：
    1. Nim积定义：a⊗b = mex{(a'⊗b)⊕(a⊗b')⊕(a'⊗b') | a'<a, b'<b}
    2. 三维Nim积：sg[x][y][z] = x⊗y⊗z
    3. SG函数：整个游戏的SG值为所有开灯位置SG值的异或和
    
    Nim积性质：
    1. (a⊗b)⊗c = a⊗(b⊗c)（结合律）
    2. a⊗b = b⊗a（交换律）
    3. a⊗0 = 0
    4. a⊗1 = a
    
    对于三维博弈，位置(x,y,z)的SG值为x⊗y⊗z
    """
    # 计算Nim积
    for i in range(MAXN):
        for j in range(MAXN):
            if i == 0 or j == 0:
                nim[i][j] = 0
            else:
                # 计算i和j的Nim积
                appear = set()
                for a in range(i):
                    for b in range(j):
                        # Nim积定义：a⊗b = mex{(a'⊗b)⊕(a⊗b')⊕(a'⊗b') | a'<a, b'<b}
                        val = (nim[a][j] ^ nim[i][b] ^ nim[a][b])
                        appear.add(val)
                
                # 计算mex值
                mex = 0
                while mex in appear:
                    mex += 1
                nim[i][j] = mex
    
    # 计算每个位置的SG值
    for x in range(MAXN):
        for y in range(MAXN):
            for z in range(MAXN):
                # 位置(x,y,z)的SG值为x⊗y⊗z
                sg[x][y][z] = nim[nim[x][y]][z]

def solve(lights):
    """
    算法原理：
    根据SG函数计算整个游戏的SG值
    1. 整个游戏的SG值为所有开灯位置SG值的异或和
    2. SG值不为0表示必胜态，为0表示必败态
    """
    # 异常处理：处理空数组
    if not lights:
        return "No"  # 空游戏，先手败
    
    # 计算所有开灯位置SG值的异或和
    xor_sum = 0
    for x, y, z in lights:
        # 异常处理：处理非法坐标
        if x < 0 or x >= MAXN or y < 0 or y >= MAXN or z < 0 or z >= MAXN:
            return "输入非法"
        xor_sum ^= sg[x][y][z]
    
    # SG值不为0表示必胜态，为0表示必败态
    return "Yes" if xor_sum != 0 else "No"

# 预处理Nim积
build()

# 测试示例
if __name__ == "__main__":
    # 示例1: lights = [(1,1,1), (2,2,2)]
    # sg[1][1][1] = nim[nim[1][1]][1] = nim[1][1] = 1
    # sg[2][2][2] = nim[nim[2][2]][2] = nim[3][2] = 1
    # xor_sum = 1^1 = 0
    # 预期结果: No
    result1 = solve([(1, 1, 1), (2, 2, 2)])
    
    # 示例2: lights = [(1,2,3)]
    # sg[1][2][3] = nim[nim[1][2]][3] = nim[2][3] = 1
    # xor_sum = 1
    # 预期结果: Yes
    result2 = solve([(1, 2, 3)])
    
    print(f"示例1结果: {result1}")
    print(f"示例2结果: {result2}")