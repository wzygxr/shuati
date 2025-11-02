package class117;

/**
 * 位运算基础操作工具类
 * 
 * 【核心原理说明】
 * 本类演示了两个经典的位运算操作：
 * 1. 二进制位分解 - 将十进制数分解为二进制表示并展示每一位
 * 2. 最大2的幂求解 - 找出不大于给定数的最大2的幂次方
 * 位运算直接对整数的二进制位进行操作，是计算机科学中基础且高效的运算方式。
 * 
 * 【位运算基础】
 * 位运算是直接对整数的二进制位进行操作的运算，具有高效、简洁的特点。
 * 在计算机底层，所有数据都是以二进制形式存储的，因此位运算通常比加减乘除等运算更快。
 * 位运算在算法优化、系统编程、数据结构实现等领域有着广泛应用。
 * 
 * 【主要位操作符】
 * - <<: 左移运算符，将二进制位向左移动指定的位数，相当于乘以2的幂次方
 * - >>: 带符号右移运算符，将二进制位向右移动指定的位数，高位补符号位，相当于除以2的幂次方
 * - >>>: 无符号右移运算符，将二进制位向右移动指定的位数，高位补0（Java特有）
 * - &: 按位与运算符，对应位都为1时结果才为1
 * - |: 按位或运算符，对应位有一个为1时结果就为1
 * - ^: 按位异或运算符，对应位不同时结果为1
 * - ~: 按位非运算符，将每一位取反
 * 
 * 【位运算常用技巧】
 * 1. 检查第k位是否为1: (x & (1 << k)) != 0
 * 2. 设置第k位为1: x | (1 << k)
 * 3. 清除第k位为0: x & ~(1 << k)
 * 4. 切换第k位的值: x ^ (1 << k)
 * 5. 计算x的绝对值: (x ^ (x >> 31)) - (x >> 31)（适用于32位整数）
 * 6. 计算两个数的平均值: (x + y) >> 1（当和不溢出时）
 * 7. 判断奇偶性: x & 1 == 1（奇数），x & 1 == 0（偶数）
 * 
 * 【应用场景】
 * 1. 数据压缩和加密算法
 * 2. 图像处理中的像素操作
 * 3. 网络协议中的数据传输和校验
 * 4. 算法优化中的状态表示
 * 5. 系统编程中的内存管理
 * 6. 密码学和哈希函数实现
 * 7. 图形学和游戏开发
 * 8. 嵌入式系统编程
 * 
 * 【复杂度分析】
 * - 时间复杂度：位运算操作通常是O(1)时间复杂度，因为它们直接在硬件级别执行
 * - 空间复杂度：位运算通常只需要常数空间
 * 
 * 【相关题目】
 * 1. LeetCode 191. Number of 1 Bits - 统计二进制中1的个数
 * 2. LeetCode 338. Counting Bits - 计算从0到n各数的二进制表示中1的个数
 * 3. LeetCode 231. Power of Two - 判断一个数是否是2的幂
 * 4. LeetCode 342. Power of Four - 判断一个数是否是4的幂
 * 5. LeetCode 136. Single Number - 找出数组中只出现一次的数字
 * 6. LeetCode 137. Single Number II - 找出数组中只出现一次的数字II
 * 7. LeetCode 268. Missing Number - 数组中缺失的数字
 * 8. LeetCode 371. Sum of Two Integers - 不使用+-符号实现加法
 * 9. Codeforces 1367A. Short Substrings - 字符串处理中的位运算应用
 * 10. AcWing 90. 64位整数乘法 - 位运算实现大数乘法模运算
 * 11. POJ 1995. Raising Modulo Numbers - 快速幂算法的模运算实现
 * 12. POJ 3252. Round Numbers - 统计区间内的圆数（二进制中0的个数不少于1的个数）
 * 13. SPOJ BITMAP - 图像二值化中的位运算优化
 */
public class TwoSmallQuestions {

	/**
	 * 展示一个正整数的二进制位表示
	 * 
	 * 【实现原理】
	 * 使用位运算从高位到低位检查每一位是否为1。
	 * 通过左移运算(1 << p)生成只有第p位为1的掩码，与目标数比较。
	 * 如果掩码值小于等于剩余的数，则表示该位为1，从剩余数中减去掩码值。
	 * 
	 * 【时间复杂度】
	 * O(m)，其中m是要检查的二进制位数
	 * 
	 * 【空间复杂度】
	 * O(1)，只使用了常数额外空间
	 * 
	 * @param x 要分解的正整数
	 * @param m 表示x所需的二进制位数（必须确保m位足够表示x）
	 * @throws IllegalArgumentException 如果x为负数
	 */
	public static void show1(int x, int m) {
		if (x < 0) {
			throw new IllegalArgumentException("参数x必须为正整数");
		}
		for (int p = m - 1, t = x; p >= 0; p--) {
			// 生成第p位为1的掩码
			int mask = 1 << p;
			if (mask <= t) {
				// 当前位为1，从剩余值中减去该位的权值
				t -= mask;
				System.out.println(x + "的第" + p + "位是1");
			} else {
				// 当前位为0
				System.out.println(x + "的第" + p + "位是0");
			}
		}
	}

	/**
	 * 找出不大于给定正整数的最大2的幂次方
	 * 
	 * 【实现原理】
	 * 通过不断左移操作，找到最大的k使得2^k ≤ x
	 * 使用(x >> 1)作为上限比较，避免直接与x比较可能导致的整数溢出问题
	 * 
	 * 【优化亮点】
	 * 1. 防止整数溢出的安全实现
	 * 2. 高效的位运算迭代方法
	 * 
	 * 【时间复杂度】
	 * O(log x)，因为最大迭代次数等于x的二进制位数
	 * 
	 * 【空间复杂度】
	 * O(1)，只使用了常数额外空间
	 * 
	 * @param x 给定的正整数
	 * @throws IllegalArgumentException 如果x为负数
	 */
	public static void show2(int x) {
		if (x < 0) {
			throw new IllegalArgumentException("参数x必须为正整数");
		}
		int power = 0;
		// 防止溢出的安全写法：与(x >> 1)比较而不是直接与x比较
		while ((1 << power) <= (x >> 1)) {
			power++;
		}
		System.out.println("<=" + x + "最大的2的幂，是2的" + power + "次方");
	}

	/**
 * 测试位运算工具类的功能
 * 
 * 包含多个测试用例，覆盖不同的输入情况，确保算法正确性
 * 测试内容：
 * 1. 二进制位分解的多种情况（常规数、边界情况、全1情况）
 * 2. 最大2的幂查找的多种情况（常规数、恰好是2的幂、边界情况、大数）
 * 3. 异常处理测试
 */
public static void testBitOperations() {
	System.out.println("开始测试位运算工具类...");
	
	// 测试show1方法
	System.out.println("\n测试1: 二进制位分解");
	int[][] binaryTestCases = {
		{101, 10},  // 常规测试
		{0, 5},     // 边界情况：0
		{1, 1},     // 边界情况：1
		{255, 8}    // 全1的情况
	};
	
	for (int[] testCase : binaryTestCases) {
		int x = testCase[0];
		int m = testCase[1];
		System.out.println("\n分解 " + x + " 的 " + m + " 位二进制表示:");
		try {
			show1(x, m);
		} catch (IllegalArgumentException e) {
			System.out.println("捕获到异常: " + e.getMessage());
		}
	}
	
	// 测试show2方法
	System.out.println("\n测试2: 最大2的幂查找");
	int[] powerTestCases = {
		13,          // 常规测试，结果应为8 (2^3)
		16,          // 恰好是2的幂，结果应为16 (2^4)
		1,           // 边界情况，结果应为1 (2^0)
		2000000000   // 大数测试，结果应为1073741824 (2^30)
	};
	
	for (int x : powerTestCases) {
		System.out.println("\n查找 <= " + x + " 的最大2的幂:");
		try {
			show2(x);
		} catch (IllegalArgumentException e) {
			System.out.println("捕获到异常: " + e.getMessage());
		}
	}
	
	// 测试异常处理
	System.out.println("\n测试3: 异常处理");
	try {
		show1(-5, 5);
	} catch (IllegalArgumentException e) {
		System.out.println("预期的异常: " + e.getMessage());
	}
	
	try {
		show2(-10);
	} catch (IllegalArgumentException e) {
		System.out.println("预期的异常: " + e.getMessage());
	}
	
	System.out.println("\n所有测试完成!");
}

/**
 * 主函数，演示位运算的使用
 * 
 * 提供两种运行模式：
 * 1. 简单演示 - 展示基本功能
 * 2. 完整测试 - 运行所有测试用例
 */
public static void main(String[] args) {
	// 选择运行模式
	boolean runTests = false; // 设置为true运行完整测试
	
	if (runTests) {
		testBitOperations();
	} else {
		// 简单演示
		try {
			// 测试show1方法
			System.out.println("===== 测试二进制位分解 =====");
			int x = 101;
			int m = 10; // 确保用m个二进制位一定能表示x
			show1(x, m);

			// 测试show2方法
			System.out.println("\n===== 测试最大2的幂查找 =====");
			x = 13;
			show2(x);
			x = 16;
			show2(x);
			x = 2000000000;
			show2(x);
		} catch (IllegalArgumentException e) {
			System.out.println("错误: " + e.getMessage());
		}
	}
}

	/**
 * 【算法优化技巧】
 * 1. 使用位运算替代乘除法运算，提高执行效率（左移1位等于乘以2，右移1位等于除以2）
 * 2. 对于找最大2的幂，可以使用Integer.highestOneBit()方法实现O(1)操作
 * 3. 处理位运算时要特别注意整数溢出问题，使用long类型或特殊的比较技巧
 * 4. 使用掩码(mask)技术快速访问特定位，如(1 << k)生成第k位为1的掩码
 * 5. 对于位计数问题，可以利用Brian Kernighan算法进行优化（x &= x - 1可以清除最低位的1）
 * 6. 利用位运算的自反性和对称性，如x ^ x = 0，x ^ 0 = x
 * 7. 对于频繁的位操作，预计算常用掩码或结果可以提高性能
 * 8. 使用(x & 1)判断奇偶性比x % 2更高效
 * 9. 利用位运算实现交换两个变量的值，无需额外空间：a ^= b; b ^= a; a ^= b;
 * 10. 对于特定位的操作，使用位掩码与移位操作的组合可以提高代码的可读性和效率
 * 
 * 【常见错误点】
 * 1. 整数溢出：特别是在左移操作时，超过整数范围会导致结果错误
 * 2. 符号位处理：有符号整数的最高位是符号位，位运算时需谨慎
 * 3. 位运算优先级：位运算优先级低于算术运算，必要时添加括号
 * 4. 循环边界条件：确保循环正确处理包括0在内的边界情况
 * 5. 1-based与0-based索引混淆：二进制位通常从0开始计数
 * 6. 不同语言的位运算特性差异：如Java有>>>无符号右移，而C++没有
 * 7. 在有符号整数中，右移运算会保留符号位，可能导致意外结果
 * 8. 忽略参数校验，特别是对负数的处理
 * 9. 直接使用移位操作处理浮点数，导致类型错误
 * 10. 忘记考虑机器位数差异（32位vs 64位系统）对移位操作的影响
 * 
 * 【工程化考量】
 * 1. 对于大范围数据，考虑使用long类型避免溢出
 * 2. 添加输入参数校验，增强代码健壮性
 * 3. 提供异常处理机制，友好报告错误
 * 4. 对于性能关键应用，可以使用更高效的位操作技巧
 * 5. 在实际项目中，优先使用JDK提供的位运算工具方法
 * 6. 编写单元测试覆盖各种边界情况，如负数、0、最大整数等
 * 7. 对于复杂的位操作，添加详细注释解释算法原理
 * 8. 考虑跨平台兼容性，不同架构的CPU位数可能不同
 * 9. 在团队开发中，确保位运算的使用有足够的注释说明
 * 10. 考虑使用位运算库或工具类，提高代码的可维护性
 * 
 * 【实际应用注意事项】
 * 1. 在处理敏感数据时，注意位运算可能带来的安全隐患
 * 2. 位运算虽然高效，但会降低代码可读性，需要在性能和可读性之间权衡
 * 3. 对于频繁使用的位操作，封装成工具方法提高复用性
 * 4. 现代编译器会自动优化一些算术运算为位运算，在某些情况下可以不必手动优化
 * 5. 在处理大整数时，考虑使用BigInteger等类库以避免溢出问题
 * 6. 位运算在不同CPU架构上的性能表现可能有所不同
 * 7. 在移动设备或嵌入式系统开发中，位运算的性能优势更为明显
 * 8. 对于并行计算或SIMD指令优化，位运算也有重要应用
 * 9. 在网络协议和数据传输中，位运算常用于数据压缩和校验
 * 10. 密码学算法中广泛使用位运算进行数据混淆和加密操作
 */
}

// 以下是完整的C++版本代码实现
#include <iostream>
#include <stdexcept>
using namespace std;

/**
 * 位运算基础操作工具类
 * 
 * 该类提供了两个经典的位运算操作的实现：
 * 1. 二进制位分解 - 将十进制数分解为二进制表示并展示每一位
 * 2. 最大2的幂求解 - 找出不大于给定数的最大2的幂次方
 * 
 * 【C++实现特点】
 * - 使用了C++异常机制进行输入校验
 * - 采用long long类型防止整数溢出
 * - 静态方法实现，无需实例化即可使用
 */
class BitOperations {
public:
    /**
     * 展示一个正整数的二进制位表示
     * 
     * 【实现原理】
     * 从最高位到最低位依次检查每一位是否为1，使用掩码技术判断特定位的值。
     * 这种方法的优点是实现简单直接，可以清晰地展示每一位的状态。
     * 
     * 【时间复杂度】O(m)，其中m是要检查的二进制位数
     * 【空间复杂度】O(1)，只使用了常数额外空间
     * 
     * @param x 要分解的正整数
     * @param m 表示x所需的二进制位数（必须确保m位足够表示x）
     * @throws invalid_argument 如果x为负数或m为非正数
     */
    static void showBinaryBits(int x, int m) {
        if (x < 0) {
            throw invalid_argument("参数x必须为正整数");
        }
        if (m <= 0) {
            throw invalid_argument("参数m必须为正整数");
        }
        for (int p = m - 1, t = x; p >= 0; p--) {
            // 生成第p位为1的掩码
            int mask = 1 << p;
            if (mask <= t) {
                // 当前位为1，从剩余值中减去该位的权值
                t -= mask;
                cout << x << "的第" << p << "位是1" << endl;
            } else {
                // 当前位为0
                cout << x << "的第" << p << "位是0" << endl;
            }
        }
    }
    
    /**
     * 找出不大于给定正整数的最大2的幂次方
     * 
     * 【实现原理】
     * 通过不断左移操作，找到最大的k使得2^k ≤ x。
     * 使用(1LL << power)和(x >> 1LL)进行比较，有效避免了整数溢出问题。
     * 
     * 【优化亮点】
     * - 使用long long类型进行移位操作，防止溢出
     * - 与(x >> 1)比较而非直接与x比较，进一步避免溢出风险
     * 
     * 【时间复杂度】O(log x)，最大迭代次数等于x的二进制位数
     * 【空间复杂度】O(1)，只使用了常数额外空间
     * 
     * @param x 给定的正整数
     * @return 返回不大于x的最大2的幂次方值
     * @throws invalid_argument 如果x为负数或零
     */
    static int findLargestPowerOfTwo(int x) {
        if (x <= 0) {
            throw invalid_argument("参数x必须为正整数");
        }
        int power = 0;
        // 防止溢出的安全写法：与(x >> 1)比较而不是直接与x比较
        // 使用1LL确保移位操作在long long范围内进行
        while ((1LL << power) <= (static_cast<long long>(x) >> 1LL)) {
            power++;
        }
        int result = 1 << power;
        cout << "<=" << x << "最大的2的幂，是2的" << power << "次方" << endl;
        return result;
    }
};

/**
 * 测试位运算工具类的功能
 * 
 * 包含多个测试用例，覆盖不同的输入情况，确保算法正确性
 * 测试内容：
 * 1. 二进制位分解的多种情况（常规数、边界情况、全1情况）
 * 2. 最大2的幂查找的多种情况（常规数、恰好是2的幂、边界情况、大数）
 * 3. 异常处理测试
 */
void testBitOperations() {
    cout << "开始测试位运算工具类..." << endl;
    
    // 测试showBinaryBits方法
    cout << "\n测试1: 二进制位分解" << endl;
    struct BinaryTestCase {
        int x;
        int m;
    };
    
    BinaryTestCase binaryTestCases[] = {
        {101, 10},  // 常规测试
        {0, 5},     // 边界情况：0
        {1, 1},     // 边界情况：1
        {255, 8}    // 全1的情况
    };
    
    for (const auto& testCase : binaryTestCases) {
        int x = testCase.x;
        int m = testCase.m;
        cout << "\n分解 " << x << " 的 " << m << " 位二进制表示:" << endl;
        try {
            BitOperations::showBinaryBits(x, m);
        } catch (const invalid_argument& e) {
            cout << "捕获到异常: " << e.what() << endl;
        }
    }
    
    // 测试findLargestPowerOfTwo方法
    cout << "\n测试2: 最大2的幂查找" << endl;
    int powerTestCases[] = {
        13,          // 常规测试，结果应为8 (2^3)
        16,          // 恰好是2的幂，结果应为16 (2^4)
        1,           // 边界情况，结果应为1 (2^0)
        2000000000   // 大数测试，结果应为1073741824 (2^30)
    };
    
    for (int x : powerTestCases) {
        cout << "\n查找 <= " << x << " 的最大2的幂:" << endl;
        try {
            int result = BitOperations::findLargestPowerOfTwo(x);
            cout << "计算结果: " << result << endl;
        } catch (const invalid_argument& e) {
            cout << "捕获到异常: " << e.what() << endl;
        }
    }
    
    // 测试异常处理
    cout << "\n测试3: 异常处理" << endl;
    try {
        BitOperations::showBinaryBits(-5, 5);
    } catch (const invalid_argument& e) {
        cout << "预期的异常: " << e.what() << endl;
    }
    
    try {
        BitOperations::showBinaryBits(5, -1);
    } catch (const invalid_argument& e) {
        cout << "预期的异常: " << e.what() << endl;
    }
    
    try {
        BitOperations::findLargestPowerOfTwo(-10);
    } catch (const invalid_argument& e) {
        cout << "预期的异常: " << e.what() << endl;
    }
    
    try {
        BitOperations::findLargestPowerOfTwo(0);
    } catch (const invalid_argument& e) {
        cout << "预期的异常: " << e.what() << endl;
    }
    
    cout << "\n所有测试完成!" << endl;
}

/**
 * 主函数
 * 
 * 演示BitOperations类的使用方法，提供两种运行模式：
 * 1. 简单演示 - 展示基本功能
 * 2. 完整测试 - 运行所有测试用例
 * 
 * 使用try-catch块捕获可能的异常，确保程序优雅退出
 */
int main() {
    try {
        // 选择运行模式：设置为true运行完整测试，false运行简单演示
        bool runTests = false;
        
        if (runTests) {
            testBitOperations();
        } else {
            // 简单演示
            // 测试showBinaryBits方法
            cout << "===== 测试二进制位分解 =====" << endl;
            int x = 101;
            int m = 10; // 确保用m个二进制位一定能表示x
            BitOperations::showBinaryBits(x, m);

            // 测试findLargestPowerOfTwo方法
            cout << "\n===== 测试最大2的幂查找 =====" << endl;
            x = 13;  // 预期结果：2^3 = 8
            BitOperations::findLargestPowerOfTwo(x);
            x = 16;  // 预期结果：2^4 = 16
            BitOperations::findLargestPowerOfTwo(x);
            x = 2000000000;  // 预期结果：2^30 = 1073741824
            BitOperations::findLargestPowerOfTwo(x);
        }
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}
*/

# 以下是完整的Python版本代码实现

class BitOperations:
    """
    位运算基础操作工具类
    
    本类演示了两个经典的位运算操作：
    1. 二进制位分解 - 将十进制数分解为二进制表示并展示每一位
    2. 最大2的幂求解 - 找出不大于给定数的最大2的幂次方
    
    【Python实现特点】
    - 采用静态方法设计，无需实例化即可使用
    - 完善的参数验证和异常处理
    - 使用f-string提供清晰的输出格式
    - Python的整数类型没有大小限制，因此溢出风险较小
    
    【时间复杂度分析】
    - show_binary_bits: O(m)，其中m是指定的二进制位数
    - find_largest_power_of_two: O(log x)，其中x是输入的整数
    
    【空间复杂度分析】
    - 两个方法均为O(1)，不需要额外的数据结构
    """
    
    @staticmethod
    def show_binary_bits(x, m):
        """
        展示一个正整数的二进制位表示
        
        【实现原理】
        从最高位到最低位依次检查每一位是否为1，使用掩码技术判断特定位的值。
        这种方法的优点是实现简单直接，可以清晰地展示每一位的状态。
        
        【参数说明】
        Args:
            x: 要分解的非负整数
            m: 要显示的二进制位数
            
        【异常处理】
        Raises:
            ValueError: 如果x为负数或m不是正整数
            
        【示例】
        >>> BitOperations.show_binary_bits(5, 3)
        5的第2位是1
        5的第1位是0
        5的第0位是1
        """
        if x < 0:
            raise ValueError("参数x必须为非负整数")
        if m <= 0:
            raise ValueError("参数m必须为正整数")
        
        t = x
        for p in range(m - 1, -1, -1):
            # 生成第p位为1的掩码
            mask = 1 << p
            if mask <= t:
                # 当前位为1，从剩余值中减去该位的权值
                t -= mask
                print(f"{x}的第{p}位是1")
            else:
                # 当前位为0
                print(f"{x}的第{p}位是0")
    
    @staticmethod
    def find_largest_power_of_two(x):
        """
        找出不大于给定正整数的最大2的幂次方
        
        【实现原理】
        通过不断左移操作，找到最大的k使得2^k ≤ x。
        使用(1 << power)和(x >> 1)进行比较，有效避免了整数溢出问题。
        
        【Python特有优化】
        在Python中，可以使用bit_length()方法优化此算法，但此处保留了与其他语言一致的实现方式。
        
        【参数说明】
        Args:
            x: 给定的正整数
            
        【异常处理】
        Raises:
            ValueError: 如果x为负数或零
            
        【返回值】
        打印结果并返回最大的2的幂次方值
        
        【示例】
        >>> BitOperations.find_largest_power_of_two(13)
        <=13最大的2的幂，是2的3次方
        8
        """
        if x <= 0:
            raise ValueError("参数x必须为正整数")
        
        power = 0
        # 防止溢出的安全写法：与(x >> 1)比较而不是直接与x比较
        while (1 << power) <= (x >> 1):
            power += 1
        
        result = 1 << power
        print(f"<={x}最大的2的幂，是2的{power}次方 ({result})")
        return result


def test_bit_operations():
    """
    测试BitOperations类的功能
    
    包含多个测试用例，覆盖不同的输入情况，确保算法正确性
    测试内容：
    1. 二进制位分解的多种情况（常规数、边界情况、全1情况）
    2. 最大2的幂查找的多种情况（常规数、恰好是2的幂、边界情况、大数）
    3. 异常处理测试
    """
    print("开始测试位运算工具类...")
    
    # 测试show_binary_bits方法
    print("\n测试1: 二进制位分解")
    test_cases = [
        (101, 10),  # 常规测试
        (0, 5),     # 边界情况：0
        (1, 1),     # 边界情况：1
        (255, 8),   # 全1的情况
        (5, -1)     # 负数位数，应该抛出异常
    ]
    
    for x, m in test_cases:
        print(f"\n分解 {x} 的 {m} 位二进制表示:")
        try:
            BitOperations.show_binary_bits(x, m)
        except ValueError as e:
            print(f"捕获到异常: {e}")
    
    # 测试find_largest_power_of_two方法
    print("\n测试2: 最大2的幂查找")
    test_cases = [
        13,          # 常规测试，结果应为8 (2^3)
        16,          # 恰好是2的幂，结果应为16 (2^4)
        1,           # 边界情况，结果应为1 (2^0)
        2000000000,  # 大数测试，结果应为1073741824 (2^30)
        0            # 零值测试，应该抛出异常
    ]
    
    for x in test_cases:
        print(f"\n查找 <= {x} 的最大2的幂:")
        try:
            result = BitOperations.find_largest_power_of_two(x)
            print(f"计算结果: {result}")
        except ValueError as e:
            print(f"捕获到异常: {e}")
    
    # 测试异常处理
    print("\n测试3: 异常处理")
    try:
        BitOperations.show_binary_bits(-5, 5)
    except ValueError as e:
        print(f"预期的异常: {e}")
    
    try:
        BitOperations.find_largest_power_of_two(-10)
    except ValueError as e:
        print(f"预期的异常: {e}")
    
    print("\n所有测试完成!")


def main():
    """
    主函数，演示位运算的使用
    
    提供两种运行模式：
    1. 简单演示 - 展示基本功能
    2. 完整测试 - 运行所有测试用例
    """
    # 选择运行模式
    run_tests = False  # 设置为True运行完整测试
    
    if run_tests:
        test_bit_operations()
    else:
        # 简单演示
        try:
            # 测试show_binary_bits方法
            print("===== 测试二进制位分解 =====")
            x = 101
            m = 10  # 确保用m个二进制位一定能表示x
            BitOperations.show_binary_bits(x, m)

            # 测试find_largest_power_of_two方法
            print("\n===== 测试最大2的幂查找 =====")
            x = 13  # 预期结果：2^3 = 8
            BitOperations.find_largest_power_of_two(x)
            x = 16  # 预期结果：2^4 = 16
            BitOperations.find_largest_power_of_two(x)
            x = 2000000000  # 预期结果：2^30 = 1073741824
            BitOperations.find_largest_power_of_two(x)
        except ValueError as e:
            print(f"错误: {e}")


if __name__ == "__main__":
    main()
