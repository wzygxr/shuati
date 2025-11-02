# 扩展欧几里得算法的工程化考量

## 一、异常处理

### 1. 输入验证
```java
/**
 * 验证输入参数的有效性
 */
public static boolean isValidInput(long a, long b) {
    // 检查是否为非负数（根据具体问题要求）
    if (a < 0 || b < 0) {
        System.err.println("警告: 输入包含负数");
        // 根据具体问题决定是否允许负数
    }
    
    // 检查是否全为零
    if (a == 0 && b == 0) {
        System.err.println("错误: a和b不能同时为0");
        return false;
    }
    
    return true;
}
```

### 2. 边界条件处理
```java
/**
 * 处理特殊情况
 */
public static long[] handleSpecialCases(long a, long b) {
    // 当其中一个数为0时的处理
    if (a == 0) {
        return new long[]{b, 0, 1}; // gcd(0,b) = b, 0*0 + 1*b = b
    }
    
    if (b == 0) {
        return new long[]{a, 1, 0}; // gcd(a,0) = a, 1*a + 0*0 = a
    }
    
    return null; // 非特殊情况
}
```

### 3. 无解情况处理
```java
/**
 * 判断线性同余方程是否有解
 */
public static boolean hasSolution(long a, long b, long c) {
    long gcd = gcd(Math.abs(a), Math.abs(b));
    if (c % gcd != 0) {
        System.err.println("方程 " + a + "x + " + b + "y = " + c + " 无整数解");
        return false;
    }
    return true;
}
```

## 二、性能优化

### 1. 迭代 vs 递归
```java
// 递归版本 - 简洁但可能栈溢出
public static long[] exgcdRecursive(long a, long b) {
    if (b == 0) {
        return new long[]{a, 1, 0};
    }
    long[] result = exgcdRecursive(b, a % b);
    long x = result[2];
    long y = result[1] - (a / b) * result[2];
    return new long[]{result[0], x, y};
}

// 迭代版本 - 高效且无栈溢出风险
public static long[] exgcdIterative(long a, long b) {
    long x0 = 1, y0 = 0;
    long x1 = 0, y1 = 1;
    
    while (b != 0) {
        long q = a / b;
        long r = a % b;
        
        long x = x0 - q * x1;
        long y = y0 - q * y1;
        
        a = b;
        b = r;
        x0 = x1;
        y0 = y1;
        x1 = x;
        y1 = y;
    }
    
    return new long[]{a, x0, y0};
}
```

### 2. 大数处理
```java
import java.math.BigInteger;

/**
 * 使用BigInteger处理大数情况
 */
public static BigInteger[] exgcdBig(BigInteger a, BigInteger b) {
    if (b.equals(BigInteger.ZERO)) {
        return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
    }
    
    BigInteger[] result = exgcdBig(b, a.mod(b));
    BigInteger x = result[2];
    BigInteger y = result[1].subtract(a.divide(b).multiply(result[2]));
    
    return new BigInteger[]{result[0], x, y};
}
```

### 3. 缓存优化
```java
import java.util.HashMap;
import java.util.Map;

/**
 * 带缓存的GCD计算
 */
public class CachedGCD {
    private static Map<String, Long> cache = new HashMap<>();
    
    public static long gcdWithCache(long a, long b) {
        String key = Math.min(a, b) + "," + Math.max(a, b);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        
        long result = gcd(a, b);
        cache.put(key, result);
        return result;
    }
    
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
```

## 三、调试能力

### 1. 中间结果验证
```java
/**
 * 带验证的扩展欧几里得算法
 */
public static long[] exgcdWithVerification(long a, long b) {
    long[] result = exgcdIterative(a, b);
    long gcd = result[0];
    long x = result[1];
    long y = result[2];
    
    // 验证结果正确性
    long verification = a * x + b * y;
    if (verification != gcd) {
        System.err.println("验证失败: " + a + "*" + x + " + " + b + "*" + y + " = " + verification + 
                          " ≠ " + gcd);
    }
    
    return result;
}
```

### 2. 详细日志输出
```java
/**
 * 带详细日志的扩展欧几里得算法
 */
public static long[] exgcdWithLogging(long a, long b) {
    System.out.println("计算 gcd(" + a + ", " + b + ")");
    
    long x0 = 1, y0 = 0;
    long x1 = 0, y1 = 1;
    int step = 0;
    
    while (b != 0) {
        long q = a / b;
        long r = a % b;
        
        System.out.println("步骤 " + step + ": " + a + " = " + b + " × " + q + " + " + r);
        
        long x = x0 - q * x1;
        long y = y0 - q * y1;
        
        a = b;
        b = r;
        x0 = x1;
        y0 = y1;
        x1 = x;
        y1 = y;
        
        step++;
    }
    
    System.out.println("结果: gcd = " + a + ", x = " + x0 + ", y = " + y0);
    return new long[]{a, x0, y0};
}
```

### 3. 性能监控
```java
/**
 * 带性能监控的扩展欧几里得算法
 */
public static long[] exgcdWithProfiling(long a, long b) {
    long startTime = System.nanoTime();
    
    long[] result = exgcdIterative(a, b);
    
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    
    System.out.println("执行时间: " + duration + " 纳秒");
    
    return result;
}
```

## 四、可维护性设计

### 1. 模块化设计
```java
/**
 * 扩展欧几里得算法工具类
 */
public class ExtendedEuclideanUtils {
    
    /**
     * 基础扩展欧几里得算法
     */
    public static long[] exgcd(long a, long b) {
        return exgcdIterative(a, b);
    }
    
    /**
     * 求模逆元
     */
    public static long modInverse(long a, long m) {
        long[] result = exgcd(a, m);
        if (result[0] != 1) {
            throw new IllegalArgumentException("模逆元不存在");
        }
        return (result[1] % m + m) % m;
    }
    
    /**
     * 求解线性同余方程
     */
    public static long solveLinearCongruence(long a, long b, long m) {
        long[] result = exgcd(a, m);
        long gcd = result[0];
        if (b % gcd != 0) {
            throw new IllegalArgumentException("方程无解");
        }
        long x = result[1];
        return ((x * (b / gcd)) % (m / gcd) + (m / gcd)) % (m / gcd);
    }
}
```

### 2. 统一接口设计
```java
/**
 * 统一的数论工具接口
 */
public interface NumberTheoryUtils {
    
    /**
     * 计算最大公约数
     */
    long gcd(long a, long b);
    
    /**
     * 扩展欧几里得算法
     */
    long[] exgcd(long a, long b);
    
    /**
     * 模幂运算
     */
    long modPow(long base, long exp, long mod);
    
    /**
     * 判断是否为素数
     */
    boolean isPrime(long n);
}
```

## 五、跨语言实现一致性

### 1. Java实现
```java
public class ExgcdJava {
    public static long[] exgcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] result = exgcd(b, a % b);
        long x = result[2];
        long y = result[1] - (a / b) * result[2];
        return new long[]{result[0], x, y};
    }
}
```

### 2. Python实现
```python
def exgcd(a, b):
    """
    Python版本的扩展欧几里得算法
    """
    if b == 0:
        return a, 1, 0
    gcd, x1, y1 = exgcd(b, a % b)
    x = y1
    y = x1 - (a // b) * y1
    return gcd, x, y
```

### 3. C++实现
```cpp
#include <tuple>

std::tuple<long long, long long, long long> exgcd(long long a, long long b) {
    if (b == 0) {
        return std::make_tuple(a, 1, 0);
    }
    auto [gcd, x1, y1] = exgcd(b, a % b);
    long long x = y1;
    long long y = x1 - (a / b) * y1;
    return std::make_tuple(gcd, x, y);
}
```

## 六、测试与验证

### 1. 单元测试
```java
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedEuclideanTest {
    
    @Test
    public void testExgcd() {
        long[] result = ExtendedEuclideanUtils.exgcd(30, 20);
        assertEquals(10, result[0]); // gcd
        assertEquals(1, result[1]);  // x
        assertEquals(-1, result[2]); // y
        // 验证: 30*1 + 20*(-1) = 10
        assertEquals(10, 30 * result[1] + 20 * result[2]);
    }
    
    @Test
    public void testModInverse() {
        assertEquals(3, ExtendedEuclideanUtils.modInverse(3, 11));
        // 验证: (3 * 3) % 11 = 9 % 11 = 9 ≠ 1
        // 正确的逆元应该是4，因为(3 * 4) % 11 = 12 % 11 = 1
        assertEquals(4, ExtendedEuclideanUtils.modInverse(3, 11));
    }
}
```

### 2. 性能测试
```java
public class PerformanceTest {
    
    public static void testPerformance() {
        long[] testCases = {1000, 10000, 100000, 1000000};
        
        for (long n : testCases) {
            long startTime = System.nanoTime();
            ExtendedEuclideanUtils.exgcd(n, n - 1);
            long endTime = System.nanoTime();
            
            System.out.println("n=" + n + ", 时间=" + (endTime - startTime) + "纳秒");
        }
    }
}
```

## 七、安全考虑

### 1. 输入安全
```java
/**
 * 安全的输入处理
 */
public static long[] safeExgcd(long a, long b) {
    // 检查输入范围
    if (Math.abs(a) > Long.MAX_VALUE / 2 || Math.abs(b) > Long.MAX_VALUE / 2) {
        throw new IllegalArgumentException("输入数值过大");
    }
    
    // 处理负数
    a = Math.abs(a);
    b = Math.abs(b);
    
    return exgcdIterative(a, b);
}
```

### 2. 溢出防护
```java
/**
 * 带溢出检查的计算
 */
public static long safeMultiply(long a, long b) {
    if (a == 0 || b == 0) return 0;
    if (Math.abs(a) > Long.MAX_VALUE / Math.abs(b)) {
        throw new ArithmeticException("乘法溢出");
    }
    return a * b;
}
```

## 八、文档化与使用说明

### 1. 使用示例
```java
/**
 * 使用示例:
 * 
 * // 求解 30x + 20y = gcd(30, 20)
 * long[] result = ExtendedEuclideanUtils.exgcd(30, 20);
 * System.out.println("gcd=" + result[0] + ", x=" + result[1] + ", y=" + result[2]);
 * 
 * // 求 3 在模 11 意义下的逆元
 * long inverse = ExtendedEuclideanUtils.modInverse(3, 11);
 * System.out.println("3的模11逆元=" + inverse);
 * 
 * // 求解同余方程 3x ≡ 2 (mod 7)
 * long solution = ExtendedEuclideanUtils.solveLinearCongruence(3, 2, 7);
 * System.out.println("解=" + solution);
 */
```

### 2. 常见问题排查
```markdown
## 常见问题排查

### 1. 结果为负数
**问题**: 计算出的解为负数
**解决**: 使用 (x % m + m) % m 确保结果非负

### 2. 无解情况
**问题**: 方程无解但仍返回结果
**解决**: 先检查 gcd(a,m) 是否整除 b

### 3. 性能问题
**问题**: 大数计算很慢
**解决**: 使用迭代版本，考虑BigInteger

### 4. 精度问题
**问题**: 浮点数计算不准确
**解决**: 使用整数运算，避免浮点数
```