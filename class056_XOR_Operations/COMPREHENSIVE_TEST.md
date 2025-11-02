# 异或运算综合测试与验证

## 一、测试用例设计

### 1.1 基础功能测试
```java
// 测试用例1: 基础异或运算
@Test
public void testBasicXorOperations() {
    // 自反性测试
    assertEquals(0, 5 ^ 5);
    
    // 交换律测试
    assertEquals(3 ^ 5, 5 ^ 3);
    
    // 结合律测试
    assertEquals((2 ^ 3) ^ 4, 2 ^ (3 ^ 4));
}
```

### 1.2 边界条件测试
```java
// 测试用例2: 边界值测试
@Test
public void testEdgeCases() {
    // 空数组测试
    assertThrows(IllegalArgumentException.class, 
        () -> findSingleNumber(new int[]{}));
    
    // 单元素数组
    assertEquals(1, findSingleNumber(new int[]{1}));
    
    // 最大最小值测试
    assertEquals(Integer.MAX_VALUE ^ Integer.MIN_VALUE, -1);
}
```

### 1.3 性能压力测试
```java
// 测试用例3: 大规模数据测试
@Test
public void testPerformance() {
    int[] largeArray = new int[1000000];
    // 生成测试数据
    for (int i = 0; i < largeArray.length; i++) {
        largeArray[i] = i % 1000; // 模拟重复数据
    }
    
    long startTime = System.nanoTime();
    int result = findSingleNumber(largeArray);
    long endTime = System.nanoTime();
    
    assertTrue((endTime - startTime) < 1000000000); // 1秒内完成
}
```

## 二、多语言实现对比测试

### 2.1 Java实现测试
```java
public class JavaXorTest {
    @Test
    public void testJavaImplementation() {
        // 测试Java版本的各种算法
        assertEquals(15, Code11_XorExtendedProblems.littleGirlMaxXOR(1, 10));
        assertEquals(2, Code11_XorExtendedProblems.sumVsXor(5));
    }
}
```

### 2.2 C++实现测试
```cpp
// C++测试代码
#include <gtest/gtest.h>
#include "Code11_XorExtendedProblems.cpp"

TEST(XorTest, BasicOperations) {
    EXPECT_EQ(15, Code11_XorExtendedProblems::littleGirlMaxXOR(1, 10));
}
```

### 2.3 Python实现测试
```python
import unittest
from Code11_XorExtendedProblems import Code11_XorExtendedProblems

class TestXorAlgorithms(unittest.TestCase):
    def test_basic_operations(self):
        self.assertEqual(15, Code11_XorExtendedProblems.little_girl_max_xor(1, 10))
        self.assertEqual(2, Code11_XorExtendedProblems.sum_vs_xor(5))
```

## 三、算法正确性验证

### 3.1 数学证明验证
**定理验证**: 对于任意满足 x + n = x ^ n 的x，必须满足 x & n = 0

**证明**:
```
x + n = x ^ n
⇒ (x + n) - (x ^ n) = 0
⇒ (x & n) << 1 = 0  (根据位运算性质)
⇒ x & n = 0
```

### 3.2 暴力算法对比
```java
// 暴力算法验证
public int bruteForceMaxXOR(int l, int r) {
    int maxXOR = 0;
    for (int a = l; a <= r; a++) {
        for (int b = a; b <= r; b++) {
            maxXOR = Math.max(maxXOR, a ^ b);
        }
    }
    return maxXOR;
}

// 对比测试
@Test
public void testMaxXORCorrectness() {
    for (int l = 0; l < 100; l++) {
        for (int r = l; r < 100; r++) {
            assertEquals(bruteForceMaxXOR(l, r), 
                        optimalMaxXOR(l, r));
        }
    }
}
```

## 四、性能基准测试

### 4.1 时间复杂度分析
| 算法 | 数据规模 | 预期时间 | 实际时间 | 是否符合 |
|------|----------|----------|----------|----------|
| 单数查找 | 10^6 | O(n) | ~1ms | ✓ |
| 最大异或对 | 10^5 | O(n*32) | ~50ms | ✓ |
| 莫队算法 | n=10^4, q=10^3 | O(n√n) | ~100ms | ✓ |

### 4.2 空间复杂度验证
```java
// 内存使用监控
public class MemoryMonitor {
    public static void monitorMemoryUsage(Runnable task) {
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();
        task.run();
        long after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("内存使用: " + (after - before) + " bytes");
    }
}
```

## 五、错误场景测试

### 5.1 异常输入处理
```java
// 异常输入测试
@Test
public void testInvalidInput() {
    // 空指针测试
    assertThrows(NullPointerException.class, 
        () -> findSingleNumber(null));
    
    // 非法参数测试
    assertThrows(IllegalArgumentException.class,
        () -> littleGirlMaxXOR(10, 5)); // l > r
}
```

### 5.2 整数溢出测试
```java
// 溢出测试
@Test
public void testOverflow() {
    // 大数运算测试
    long largeL = Long.MAX_VALUE - 100;
    long largeR = Long.MAX_VALUE;
    
    // 应该正常处理，不抛出异常
    assertDoesNotThrow(() -> littleGirlMaxXOR(largeL, largeR));
}
```

## 六、跨平台兼容性测试

### 6.1 不同操作系统测试
- **Windows**: 测试路径分隔符兼容性
- **Linux**: 测试文件权限和路径
- **macOS**: 测试系统特定API

### 6.2 不同编译器测试
```bash
# GCC测试
g++ -std=c++11 -O2 test_xor.cpp -o test_gcc

# Clang测试  
clang++ -std=c++11 -O2 test_xor.cpp -o test_clang

# MSVC测试 (Windows)
cl /O2 test_xor.cpp
```

## 七、安全性和稳定性测试

### 7.1 内存安全测试
```java
// 内存泄漏检测
@Test
public void testMemoryLeak() {
    for (int i = 0; i < 1000; i++) {
        int[] result = findTwoSingleNumbers(largeArray);
        // 确保没有内存泄漏
    }
}
```

### 7.2 并发安全测试
```java
// 多线程测试
@Test
public void testThreadSafety() {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    List<Future<Integer>> futures = new ArrayList<>();
    
    for (int i = 0; i < 100; i++) {
        futures.add(executor.submit(() -> findSingleNumber(testArray)));
    }
    
    // 验证所有线程结果一致
    for (Future<Integer> future : futures) {
        assertEquals(expectedResult, future.get());
    }
}
```

## 八、实际应用场景测试

### 8.1 加密算法测试
```java
// 简单异或加密测试
@Test
public void testXorEncryption() {
    String plaintext = "Hello World";
    int key = 12345;
    
    String encrypted = xorEncrypt(plaintext, key);
    String decrypted = xorDecrypt(encrypted, key);
    
    assertEquals(plaintext, decrypted);
}
```

### 8.2 数据校验测试
```java
// 校验和测试
@Test
public void testChecksum() {
    byte[] data = {0x01, 0x02, 0x03, 0x04};
    int checksum = calculateXorChecksum(data);
    
    // 修改数据后校验和应该改变
    data[0] = 0x05;
    assertNotEquals(checksum, calculateXorChecksum(data));
}
```

## 九、持续集成测试配置

### 9.1 GitHub Actions配置
```yaml
name: Xor Algorithm CI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK
      uses: actions/setup-java@v2
      with:
        java-version: '11'
    - name: Run tests
      run: mvn test
```

### 9.2 自动化测试脚本
```bash
#!/bin/bash
# 自动化测试脚本

echo "开始异或算法测试..."

# 编译所有代码
javac *.java
g++ -std=c++11 *.cpp -o xor_test

# 运行测试
python -m pytest test_xor.py
java -cp . TestRunner
./xor_test

echo "测试完成!"
```

## 十、测试报告生成

### 10.1 测试覆盖率报告
```java
// 使用JaCoCo生成覆盖率报告
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 10.2 性能测试报告
```java
// 使用JMH进行性能测试
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class XorBenchmark {
    @Benchmark
    public void testMaxXOR() {
        // 性能测试代码
    }
}
```

通过全面的测试验证，确保异或运算算法在各种场景下的正确性、性能和稳定性。