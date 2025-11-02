package class117;

/**
 * Sparse Table（稀疏表）算法详解 - 区间GCD查询
 * 
 * 【算法核心思想】
 * Sparse Table同样适用于区间GCD查询，因为GCD运算满足可重复贡献性质
 * gcd(a,b,c) = gcd(gcd(a,b), gcd(b,c))，因此可以通过两个重叠区间的GCD合并得到整个区间的GCD
 * 这种可重复贡献性使得我们可以使用动态规划的方法预处理所有可能的2的幂次长度的区间GCD值
 * 然后在查询时，只需找到两个覆盖整个查询区间的预处理区间，计算它们的GCD即可
 * 
 * 【核心原理】
 * 基于倍增思想，我们预处理每个位置i开始，长度为2^j的区间的GCD值
 * 通过将大区间拆分为两个较小的区间（每个长度为2^(j-1)），利用已经计算好的子区间结果进行合并
 * 这种自底向上的动态规划方法确保了高效的预处理过程
 * 
 * 【位运算常用技巧】
 * 1. 左移运算：1 << k 等价于 2^k
 * 2. 右移运算：n >> 1 等价于 n / 2（整数除法）
 * 3. 位运算优先级：位移运算符优先级低于算术运算符，需要注意括号使用
 * 4. 二进制位数计算：使用位运算快速计算整数的二进制位数
 * 5. 模运算优化：对于2的幂次，可以使用位运算进行模运算
 * 6. 区间长度计算：利用位运算快速确定区间的最大覆盖长度
 * 7. 快速幂运算：通过位运算实现快速幂算法
 * 
 * 【时间复杂度分析】
 * - 预处理时间复杂度：O(n log n * log(max(arr))) - 额外的log因子来自GCD运算的时间
 *   预处理log数组需要O(n)时间
 *   ST表构建需要O(n log n)次GCD运算
 *   每次GCD运算的时间复杂度为O(log(max(arr)))
 * - 查询时间复杂度：O(log(max(arr))) - 每次查询需要两次GCD运算
 * - 空间复杂度：O(n log n) - 存储ST表和log数组
 * 
 * 【应用场景】
 * 1. 静态数组的区间GCD查询
 * 2. 区间公约数问题
 * 3. 数列分解和因子分析
 * 4. 密码学中的数论分析
 * 5. 数据压缩中的模式识别
 * 6. 图像处理中的纹理分析
 * 7. 信号处理中的特征提取
 * 
 * 【相关题目】
 * 1. 洛谷P1890 - 给定数组，多次查询区间GCD
 * 2. LeetCode 1250 - 检查「好数组」（与GCD性质相关）
 * 3. Codeforces 1343D - Constant Palindrome Sum（可使用ST表优化查询）
 * 4. SPOJ GCDEX - GCD Extreme（可结合ST表预处理）
 * 5. UVA 11417 - GCD（区间GCD相关问题）
 * 6. Codeforces 475D - CGCDSSQ（区间GCD查询的扩展应用）
 * 7. POJ 1305 - The Last Non-zero Digit（GCD相关问题）
 * 8. POJ 2429 - GCD & LCM Inverse（GCD性质应用）
 * 9. Codeforces 1295E - Permutation Separation（GCD优化问题）
 * 10. AtCoder ABC162 F - Select Half（GCD相关优化）
 * 11. HDU 5902 - GCD is Fun（区间GCD计数）
 * 12. spoj LCMSUM - LCM Sum（结合GCD预处理）
 * 13. spoj GCD2 - GCD2（大数GCD）
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_SparseTableGCD {

	// 最大数据规模
	public static int MAXN = 100001;

	// 2的17次方是>=100001且最小的
	// 所以次方可能是0~17
	// 于是准备18长度够用了
	public static int LIMIT = 18;

	// 存储原始数组数据
	public static int[] arr = new int[MAXN];

	// log2[i] : 查询<=i情况下，最大的2的幂，是2的几次方
	public static int[] log2 = new int[MAXN];

	// GCD的ST表：stgcd[i][j]表示从位置i开始，长度为2^j的区间的最大公约数
	public static int[][] stgcd = new int[MAXN][LIMIT];

	/**
	 * 求最大公约数（GCD）
	 * 使用欧几里得算法（辗转相除法）
	 * 
	 * 【算法原理】
	 * 基于数学性质：gcd(a, b) = gcd(b, a % b)
	 * 当b为0时，a即为最大公约数
	 * 
	 * 【时间复杂度】
	 * O(log(min(a,b))) - 每次迭代a%b操作会使得数值大幅减小
	 * 
	 * 【空间复杂度】
	 * O(log(min(a,b))) - 递归调用栈的深度
	 * 
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return 最大公约数
	 */
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/**
	 * 构建GCD的Sparse Table
	 * 
	 * 【实现原理】
	 * 1. 首先预处理log2数组，用于快速计算区间长度对应的最大2的幂次
	 * 2. 初始化ST表的第一层（j=0），即长度为1的区间，此时每个位置的GCD就是其自身
	 * 3. 使用动态规划的方式自底向上构建ST表：
	 *    - 对于每个幂次p，表示区间长度为2^p
	 *    - 对于每个起始位置i，确保区间不越界
	 *    - 当前区间的GCD由两个长度为2^(p-1)的子区间的GCD合并而来
	 * 
	 * 【时间复杂度】
	 * O(n log n * log(max(arr))) - 其中log(max(arr))是GCD运算的平均时间复杂度
	 * 
	 * 【空间复杂度】
	 * O(n log n) - ST表的空间占用
	 * 
	 * @param n 数组长度
	 */
	public static void build(int n) {
		// 预处理log2数组：log2[i]表示不超过i的最大2的幂次的指数
		log2[0] = -1; // 边界条件处理
		for (int i = 1; i <= n; i++) {
			// 使用位移运算高效计算log2值
			log2[i] = log2[i >> 1] + 1;
			// 初始化长度为1的区间（j=0），此时区间GCD就是元素本身
			stgcd[i][0] = arr[i];
		}
		
		// 动态规划构建ST表
		// p表示区间长度为2^p
		for (int p = 1; p <= log2[n]; p++) {
			// i表示区间起始位置，确保区间不越界
			for (int i = 1; i + (1 << p) - 1 <= n; i++) {
				// 计算两个子区间的起始位置
				// 第一个子区间：[i, i+2^(p-1)-1]
				// 第二个子区间：[i+2^(p-1), i+2^p-1]
				int mid = i + (1 << (p - 1));
				// 状态转移：当前区间的GCD由两个子区间的GCD合并而来
				stgcd[i][p] = gcd(stgcd[i][p - 1], stgcd[mid][p - 1]);
			}
		}
	}

	/**
	 * 查询区间[l,r]的最大公约数
	 * 
	 * 【实现原理】
	 * 1. 计算查询区间的长度：len = r - l + 1
	 * 2. 找到最大的p，使得 2^p ≤ len
	 * 3. 构造两个覆盖整个查询区间的预处理区间：
	 *    - 第一个区间：从l开始，长度为2^p
	 *    - 第二个区间：以r结束，长度为2^p
	 * 4. 这两个区间的GCD即为整个查询区间的GCD
	 * 
	 * 【区间覆盖示例】
	 * 假设查询区间长度为5，最大的2^p为4（p=2）
	 * 第一个区间：[l, l+3]，覆盖位置l, l+1, l+2, l+3
	 * 第二个区间：[r-3, r]，覆盖位置r-3, r-2, r-1, r
	 * 合并后完全覆盖[l, r]区间
	 * 
	 * 【时间复杂度】
	 * O(log(max(arr))) - 主要来自两次GCD运算
	 * 
	 * 【空间复杂度】
	 * O(1) - 只使用常数额外空间
	 * 
	 * @param l 区间左边界（1-based）
	 * @param r 区间右边界（1-based）
	 * @return 区间最大公约数
	 */
	public static int query(int l, int r) {
		// 计算区间长度对应的最大2的幂次
		int p = log2[r - l + 1];
		// 计算第二个区间的起始位置：r - 2^p + 1
		int start = r - (1 << p) + 1;
		// 找到两个覆盖整个查询区间的预处理区间，计算它们的GCD
		return gcd(stgcd[l][p], stgcd[start][p]);
	}

	/**
	 * 扩展功能：验证区间是否所有元素都能被某个数整除
	 * 
	 * 【数学原理】
	 * 如果一个数d能整除区间的最大公约数g，那么d能整除区间中的所有数
	 * 这是因为区间中的每个数都是g的倍数，而g是d的倍数
	 * 
	 * 【应用场景】
	 * 1. 检查区间中的数是否都有共同因子
	 * 2. 验证区间是否可被特定数整除
	 * 3. 寻找区间中的公共因子
	 * 
	 * 【时间复杂度】
	 * O(log(max(arr))) - 来自query操作和取模运算
	 * 
	 * @param l 区间左边界（1-based）
	 * @param r 区间右边界（1-based）
	 * @param divisor 除数
	 * @return 是否所有元素都能被divisor整除
	 */
	public static boolean checkDivisible(int l, int r, int divisor) {
		// 如果区间GCD能被divisor整除，则所有元素都能被divisor整除
		return query(l, r) % divisor == 0;
	}

	/**
	 * 主函数 - 处理输入输出
	 * 对应题目：洛谷P1890
	 * 
	 * 【输入输出优化】
	 * 使用BufferedReader、StreamTokenizer和PrintWriter组合进行高效的输入输出处理
	 * 特别是对于大数据量的情况，这种方式比Scanner和System.out.println更高效
	 * 
	 * 【流程说明】
	 * 1. 读取数组长度n和查询次数m
	 * 2. 读取数组元素到全局数组arr
	 * 3. 构建GCD的Sparse Table
	 * 4. 处理每个查询，输出结果
	 * 5. 关闭资源
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入输出方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和查询次数m
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		
		// 读取数组元素（1-based索引）
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		
		// 构建Sparse Table
		build(n);
		
		// 处理每个查询
		for (int i = 1, l, r; i <= m; i++) {
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval;
			// 输出查询结果
			out.println(query(l, r));
		}
		
		// 刷新输出缓冲区，确保所有数据都被写入
		out.flush();
		// 关闭资源，避免内存泄漏
		out.close();
		br.close();
	}

	/**
	 * 【算法优化技巧】
	 * 1. 预处理log数组避免重复计算，使用位运算提高效率
	 * 2. 递归形式的GCD实现简洁高效，在Java中尾递归可能被优化
	 * 3. 使用位移运算替代乘法和除法，提高位运算效率
	 * 4. 对于大数据量，可以考虑使用非递归的GCD实现避免栈溢出
	 * 5. 在预处理ST表时，可以按位运算预计算所有可能的区间长度
	 * 6. 使用1-based索引设计，避免数组边界检查的复杂性
	 * 7. 对于稀疏表的实现，可以使用更紧凑的数据结构减少缓存未命中
	 * 8. 利用GCD的性质（如gcd(a,b,c) = gcd(gcd(a,b),c)）优化计算
	 * 9. 可以预先计算最大可能的log值，避免运行时重复计算
	 * 10. 使用局部变量存储中间结果，减少数组访问次数
	 * 
	 * 【常见错误点】
	 * 1. 数组索引越界：在构建和查询ST表时没有正确检查边界条件
	 * 2. 整数溢出：对于较大的数，位移运算可能导致溢出
	 * 3. log2数组初始化错误：特别是log2[0]的处理
	 * 4. GCD计算中的错误处理：例如没有考虑0的情况
	 * 5. 位运算优先级问题：位移运算符优先级低于算术运算符，需要注意括号使用
	 * 6. 递归深度过大：在大数据量的GCD计算中可能导致栈溢出
	 * 7. 内存分配不足：对于非常大的数组，ST表可能需要过多内存
	 * 8. 区间边界处理错误：尤其是在转换1-based和0-based索引时
	 * 9. 查询区间长度计算错误：导致选择了错误的k值
	 * 10. 输入输出效率问题：大数据量情况下没有使用高效的IO方式
	 * 
	 * 【工程化考量】
	 * 1. 异常处理：添加输入参数校验，处理无效查询
	 * 2. 内存优化：对于特别大的数组，可以考虑动态调整ST表大小
	 * 3. 并发处理：对于多线程环境，考虑添加同步机制
	 * 4. 测试覆盖：编写全面的测试用例，覆盖各种边界情况
	 * 5. 代码复用：将ST表封装为通用类，支持不同的数据类型和操作
	 * 6. 性能监控：添加性能指标收集，监控查询效率
	 * 7. 文档完善：提供详细的API文档和使用示例
	 * 8. 并行预处理：对于非常大的数据集，可以考虑并行构建ST表
	 * 9. 可扩展性：设计支持不同区间操作的数据结构框架
	 * 10. 内存布局优化：考虑缓存友好的数据访问模式
	 * 
	 * 【实际应用注意事项】
	 * 1. 数据规模评估：对于特别大的数组，需要评估内存占用是否在允许范围内
	 * 2. 查询频率分析：ST表适用于查询密集型应用，预处理一次性完成
	 * 3. 数据特性利用：如果数据有特定规律，可以进一步优化GCD计算
	 * 4. 混合策略：在某些情况下，结合不同数据结构可能更优
	 * 5. 语言特性：利用Java的包装类和泛型可以实现更灵活的ST表
	 * 6. 维护成本：确保代码的可读性和可维护性，便于后续优化
	 * 7. 硬件环境：考虑目标运行环境的内存限制和缓存大小
	 * 8. 数据动态性：ST表不支持动态更新，如果数据需要频繁修改，考虑使用线段树
	 * 9. 精度问题：处理大整数时注意溢出问题，可以考虑使用BigInteger
	 * 10. 性能测试：在实际数据集上进行性能测试，验证算法效率
	 */
}

/*
【C++版本代码】
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * @class SparseTableGCD
 * @brief Sparse Table算法实现 - 区间GCD查询
 * 
 * 该实现采用动态规划预处理所有长度为2^k的区间GCD值，
 * 实现O(1)时间复杂度的区间GCD查询。
 * 
 * 【时间复杂度】
 * - 预处理：O(n log n)
 * - 单次查询：O(1)
 * 
 * 【空间复杂度】
 * - O(n log n)
 */

// 定义常量
const int MAXN = 100001;  // 最大数据规模
const int LIMIT = 18;      // 最大幂次限制：2^17 ≈ 131072 > 100000

// 全局数组
int arr[MAXN];             // 原始数组数据
int log2_[MAXN];           // log2数组，存储最大2的幂次（避免与cmath中的log2函数冲突）
int stgcd[MAXN][LIMIT];    // GCD的ST表

/**
 * @brief 求最大公约数
 * @param a 第一个数
 * @param b 第二个数
 * @return 最大公约数
 * 
 * 使用递归形式的欧几里得算法（辗转相除法），
 * 时间复杂度：O(log min(a,b))
 */
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

/**
 * @brief 构建GCD的Sparse Table
 * @param n 数组长度
 * 
 * 1. 预处理log2_数组，使用位运算优化计算
 * 2. 初始化长度为1的区间（即每个元素自身）
 * 3. 动态规划构建所有长度为2^p的区间GCD值
 */
void build(int n) {
    // 预处理log2_数组 - 使用位运算计算整数的二进制位数
    log2_[0] = -1;  // 边界条件处理
    for (int i = 1; i <= n; i++) {
        log2_[i] = log2_[i >> 1] + 1;  // i >> 1 等价于 i / 2
        // 初始化长度为1的区间
        stgcd[i][0] = arr[i];
    }
    
    // 动态规划构建ST表 - 自底向上构建所有可能的区间长度
    for (int p = 1; p <= log2_[n]; p++) {
        // 确保区间不越界，遍历所有可能的起始位置
        for (int i = 1; i + (1 << p) - 1 <= n; i++) {
            // 计算第二个子区间的起始位置
            int mid = i + (1 << (p - 1));
            // 合并两个子区间的GCD值
            stgcd[i][p] = gcd(stgcd[i][p - 1], stgcd[mid][p - 1]);
        }
    }
}

/**
 * @brief 查询区间[l,r]的最大公约数
 * @param l 区间左边界（1-based）
 * @param r 区间右边界（1-based）
 * @return 区间最大公约数
 * 
 * 利用预处理的ST表，找到最大的k使得2^k ≤区间长度，
 * 然后查询两个覆盖整个区间的子区间的GCD。
 */
int query(int l, int r) {
    // 计算区间长度对应的最大2的幂次
    int p = log2_[r - l + 1];
    // 计算第二个区间的起始位置
    int start = r - (1 << p) + 1;
    // 合并两个区间的GCD
    return gcd(stgcd[l][p], stgcd[start][p]);
}

/**
 * @brief 扩展功能：验证区间是否所有元素都能被某个数整除
 * @param l 区间左边界（1-based）
 * @param r 区间右边界（1-based）
 * @param divisor 除数
 * @return 是否所有元素都能被divisor整除
 * 
 * 如果区间的GCD能被divisor整除，则说明区间中的所有元素都能被divisor整除。
 */
bool checkDivisible(int l, int r, int divisor) {
    return query(l, r) % divisor == 0;
}

/**
 * @brief 主函数
 * 
 * 处理输入输出，构建Sparse Table并回答查询。
 * 采用输入输出优化技巧处理大数据量。
 */
int main() {
    // 输入输出优化
    ios::sync_with_stdio(false);  // 关闭同步
    cin.tie(0);                   // 解除cin和cout的绑定
    
    int n, m;  // 数组长度和查询次数
    cin >> n >> m;
    
    // 读取数组元素（1-based索引）
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 构建Sparse Table
    build(n);
    
    // 处理每个查询
    for (int i = 1, l, r; i <= m; i++) {
        cin >> l >> r;
        cout << query(l, r) << '\n';  // 使用'\n'而不是endl以避免刷新缓冲区
    }
    
    return 0;
}

【Python版本代码】
import sys
import math

class SparseTableGCD:
    """
    稀疏表（Sparse Table）类 - 用于区间最大公约数（GCD）查询
    
    该实现使用动态规划预处理所有长度为2^k的区间GCD值，
    实现O(1)时间复杂度的区间GCD查询。
    
    【核心原理】
    1. 预处理每个位置开始，长度为2^k的区间GCD值
    2. 查询时，找到最大的k使得2^k ≤区间长度
    3. 使用两个覆盖整个查询区间的子区间的GCD值
    
    【时间复杂度】
    - 预处理: O(n log n)
    - 单次查询: O(log(max(arr)))  // GCD计算的时间
    
    【空间复杂度】
    - O(n log n)
    """
    
    def __init__(self):
        """
        初始化SparseTableGCD对象
        
        Attributes:
            arr (list): 原始数组（1-based索引）
            log2 (list): log2数组，存储整数对应的最大2的幂次
            stgcd (list): 二维数组，存储区间GCD值
            n (int): 数组长度
            limit (int): 最大幂次限制
        """
        self.arr = []       # 原始数组（1-based索引）
        self.log2 = []      # log2数组，避免重复计算
        self.stgcd = []     # GCD的ST表
        self.n = 0          # 数组长度
        self.limit = 0      # 最大幂次
    
    def gcd(self, a, b):
        """
        求两个数的最大公约数
        
        使用迭代版本的欧几里得算法（辗转相除法），
        避免递归深度过大可能导致的栈溢出问题。
        
        Args:
            a (int): 第一个整数
            b (int): 第二个整数
            
        Returns:
            int: 两个数的最大公约数
            
        【时间复杂度】
        - O(log min(a, b))
        """
        while b:
            a, b = b, a % b
        return a
    
    def build(self, data):
        """
        构建GCD的Sparse Table
        
        Args:
            data (list): 输入数组（0-based索引）
            
        【实现步骤】
        1. 将0-based输入数组转换为1-based索引（内部使用）
        2. 预处理log2数组，使用位运算优化计算
        3. 初始化ST表，并填充长度为1的区间值
        4. 动态规划构建所有长度为2^p的区间GCD值
        """
        # 调整为1-based索引（便于区间计算）
        self.n = len(data)
        self.arr = [0] * (self.n + 1)  # arr[0]未使用
        for i in range(self.n):
            self.arr[i + 1] = data[i]
        
        # 预处理log2数组 - 使用位运算计算
        self.log2 = [0] * (self.n + 1)
        self.log2[0] = -1  # 边界条件
        for i in range(1, self.n + 1):
            self.log2[i] = self.log2[i // 2] + 1  # i // 2 等价于 i >> 1
        
        # 计算最大需要的幂次
        self.limit = self.log2[self.n] + 1
        
        # 初始化ST表
        self.stgcd = [[0] * self.limit for _ in range(self.n + 1)]
        
        # 构建ST表 - 自底向上动态规划
        # 初始化长度为1的区间（每个元素自身）
        for i in range(1, self.n + 1):
            self.stgcd[i][0] = self.arr[i]
        
        # 构建更长的区间
        for p in range(1, self.limit):
            # 遍历所有可能的起始位置，确保区间不越界
            for i in range(1, self.n - (1 << p) + 2):
                # 计算第二个子区间的起始位置
                mid = i + (1 << (p - 1))
                # 合并两个子区间的GCD
                self.stgcd[i][p] = self.gcd(
                    self.stgcd[i][p-1], 
                    self.stgcd[mid][p-1]
                )
    
    def query(self, l, r):
        """
        查询区间[l,r]的最大公约数
        
        Args:
            l (int): 区间左边界（0-based索引）
            r (int): 区间右边界（0-based索引）
            
        Returns:
            int: 区间[l,r]的最大公约数
            
        Raises:
            ValueError: 如果区间无效（l > r或越界）
            
        【查询逻辑】
        1. 将0-based索引转换为内部使用的1-based索引
        2. 计算区间长度len = r - l + 1
        3. 找到最大的k使得2^k ≤ len
        4. 查询两个覆盖整个区间的子区间的GCD
        """
        # 检查区间有效性
        if l > r or l < 0 or r >= self.n:
            raise ValueError(f"Invalid query range: [{l}, {r}]")
        
        # 转换为1-based索引
        l += 1
        r += 1
        
        # 计算区间长度对应的最大2的幂次
        p = self.log2[r - l + 1]
        # 计算第二个区间的起始位置
        start = r - (1 << p) + 1
        # 合并两个区间的GCD
        return self.gcd(self.stgcd[l][p], self.stgcd[start][p])
    
    def check_divisible(self, l, r, divisor):
        """
        检查区间[l,r]内的所有元素是否都能被divisor整除
        
        【数学原理】
        如果区间的GCD能被divisor整除，那么区间内的所有元素都能被divisor整除。
        
        Args:
            l (int): 区间左边界（0-based索引）
            r (int): 区间右边界（0-based索引）
            divisor (int): 除数
            
        Returns:
            bool: 如果区间内所有元素都能被divisor整除返回True，否则返回False
            
        Raises:
            ValueError: 如果divisor为0或区间无效
        """
        if divisor == 0:
            raise ValueError("Divisor cannot be zero")
        
        # 利用GCD的性质：如果区间GCD能被divisor整除，所有元素都能被整除
        return self.query(l, r) % divisor == 0


def test_sparse_table():
    """
    测试SparseTableGCD类的功能
    
    包含多种边界情况和典型用例的测试。
    """
    # 测试用例1：基本功能测试
    data1 = [1, 2, 3, 4, 5]
    st1 = SparseTableGCD()
    st1.build(data1)
    
    # 验证查询结果
    assert st1.query(0, 4) == 1  # 整个数组的GCD
    assert st1.query(0, 2) == 1  # [1,2,3]的GCD
    assert st1.query(1, 3) == 1  # [2,3,4]的GCD
    assert st1.query(2, 2) == 3  # 单个元素的GCD
    
    # 测试用例2：具有公因子的数组
    data2 = [6, 12, 18, 24, 30]
    st2 = SparseTableGCD()
    st2.build(data2)
    
    assert st2.query(0, 4) == 6  # 整个数组的GCD是6
    assert st2.query(1, 3) == 6  # [12,18,24]的GCD是6
    assert st2.check_divisible(0, 4, 2) == True  # 所有元素都能被2整除
    assert st2.check_divisible(0, 4, 3) == True  # 所有元素都能被3整除
    assert st2.check_divisible(0, 4, 5) == False  # 不是所有元素都能被5整除
    
    # 测试用例3：单一元素数组
    data3 = [100]
    st3 = SparseTableGCD()
    st3.build(data3)
    
    assert st3.query(0, 0) == 100  # 单个元素的GCD
    
    # 测试用例4：所有元素相同的数组
    data4 = [7, 7, 7, 7, 7]
    st4 = SparseTableGCD()
    st4.build(data4)
    
    assert st4.query(0, 4) == 7  # 整个数组的GCD是7
    assert st4.query(1, 3) == 7  # [7,7,7]的GCD是7
    
    print("All tests passed!")


def main():
    """
    主函数 - 处理输入输出（针对编程竞赛优化）
    
    【输入输出优化】
    1. 一次性读取所有输入数据
    2. 收集所有输出结果，一次性打印
    3. 避免频繁的I/O操作，提高大数据量处理效率
    """
    # 读取输入（一次性读取所有数据，提高效率）
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 读取数组数据（0-based）
    data = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 构建Sparse Table
    st = SparseTableGCD()
    st.build(data)
    
    # 处理查询并收集结果
    output = []
    for _ in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        # 注意：编程竞赛中输入通常为1-based索引，需要转换为0-based
        result = st.query(l - 1, r - 1)
        output.append(str(result))
    
    # 一次性输出所有结果，减少I/O次数
    print('\n'.join(output))

# 运行测试
if __name__ == "__main__":
    # 可以选择运行测试或主程序
    # test_sparse_table()  # 取消注释运行测试
    main()

# 更多测试用例
'''
测试用例1：
输入：
5 3
1 2 3 4 5
1 5
1 3
2 4

期望输出：
1
1
1

测试用例2：
输入：
5 2
6 12 18 24 30
1 5
2 4

期望输出：
6
6

测试用例3（边界情况）：
输入：
1 1
100
1 1

期望输出：
100

期望输出：
6
6
'''

*/
