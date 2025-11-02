# 单调队列优化动态规划工程化异常处理指南

## 一、异常处理原则

### 1.1 防御性编程
- **输入验证**：所有外部输入必须经过严格验证
- **边界检查**：数组索引、数值范围等必须检查
- **状态检查**：算法执行过程中的状态必须验证

### 1.2 错误分类
- **输入错误**：参数不合法、数据格式错误
- **计算错误**：数值溢出、除零错误
- **逻辑错误**：无解情况、算法执行失败

## 二、输入验证模板

### 2.1 参数范围验证
```java
/**
 * 参数验证方法
 */
private static void validateParameters(int n, int a, int b) {
    // 非负性检查
    if (n < 0 || n >= MAXN) {
        throw new IllegalArgumentException("n必须在[0, " + (MAXN - 1) + "]范围内");
    }
    
    // 正数检查
    if (a <= 0 || b <= 0) {
        throw new IllegalArgumentException("a和b必须为正数");
    }
    
    // 逻辑关系检查
    if (a > b) {
        throw new IllegalArgumentException("a不能大于b");
    }
    
    // 边界关系检查
    if (b > n) {
        throw new IllegalArgumentException("b不能大于n");
    }
}
```

### 2.2 数组数据验证
```java
/**
 * 数组数据验证
 */
private static void validateArray(int[] arr, int expectedLength) {
    if (arr == null) {
        throw new IllegalArgumentException("数组不能为null");
    }
    
    if (arr.length != expectedLength) {
        throw new IllegalArgumentException("数组长度必须为" + expectedLength);
    }
    
    // 检查数组元素范围
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] < MIN_VALUE || arr[i] > MAX_VALUE) {
            throw new IllegalArgumentException("数组元素必须在[" + MIN_VALUE + ", " + MAX_VALUE + "]范围内");
        }
    }
}
```

## 三、边界条件处理

### 3.1 特殊边界情况
```java
// 处理n=0的特殊情况
if (n == 0) {
    return handleZeroCase();
}

// 处理a=b=1的特殊情况（简化为前缀和问题）
if (a == 1 && b == 1) {
    return computePrefixSum(arr);
}

// 处理窗口大小等于数组长度的情况
if (k == n) {
    return Arrays.stream(nums).max().getAsInt();
}
```

### 3.2 数值溢出检查
```java
/**
 * 安全加法，防止整数溢出
 */
private static int safeAdd(int a, int b) {
    long result = (long) a + b;
    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
        throw new ArithmeticException("整数溢出: " + a + " + " + b);
    }
    return (int) result;
}

/**
 * 安全乘法，防止整数溢出
 */
private static int safeMultiply(int a, int b) {
    long result = (long) a * b;
    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
        throw new ArithmeticException("整数溢出: " + a + " * " + b);
    }
    return (int) result;
}
```

## 四、算法执行状态监控

### 4.1 队列状态检查
```java
/**
 * 检查队列状态是否合法
 */
private static void validateQueueState(int l, int r, int queueSize) {
    if (l < 0 || r < -1 || l > r + 1) {
        throw new IllegalStateException("队列指针状态异常: l=" + l + ", r=" + r);
    }
    
    if (r - l + 1 > queueSize) {
        throw new IllegalStateException("队列大小超出限制");
    }
}
```

### 4.2 DP状态验证
```java
/**
 * 验证DP数组状态
 */
private static void validateDPState(int[] dp, int currentIndex) {
    if (currentIndex < 0 || currentIndex >= dp.length) {
        throw new IndexOutOfBoundsException("DP索引越界: " + currentIndex);
    }
    
    // 检查DP值是否在合理范围内
    if (dp[currentIndex] != NA && 
        (dp[currentIndex] > MAX_DP_VALUE || dp[currentIndex] < MIN_DP_VALUE)) {
        throw new ArithmeticException("DP值超出合理范围: " + dp[currentIndex]);
    }
}
```

## 五、异常处理最佳实践

### 5.1 主函数异常处理
```java
public static void main(String[] args) {
    try {
        // 读取输入
        readInput();
        
        // 参数验证
        validateParameters();
        
        // 执行算法
        int result = compute();
        
        // 输出结果
        System.out.println(result);
        
    } catch (IllegalArgumentException e) {
        System.err.println("输入错误: " + e.getMessage());
        System.exit(1);
    } catch (ArithmeticException e) {
        System.err.println("计算错误: " + e.getMessage());
        System.exit(1);
    } catch (IllegalStateException e) {
        System.err.println("程序状态错误: " + e.getMessage());
        System.exit(1);
    } catch (Exception e) {
        System.err.println("未知错误: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}
```

### 5.2 调试信息输出
```java
/**
 * 调试模式下的状态输出
 */
private static void debugPrint(String message, Object... args) {
    if (DEBUG) {
        System.err.printf("[DEBUG] " + message + "%n", args);
    }
}

/**
 * 打印队列状态（用于调试）
 */
private static void printQueueState(int[] queue, int l, int r, int[] dp) {
    if (DEBUG) {
        System.err.print("队列状态: [");
        for (int i = l; i <= r; i++) {
            System.err.printf("(%d,%d)", queue[i], dp[queue[i]]);
            if (i < r) System.err.print(", ");
        }
        System.err.println("]");
    }
}
```

## 六、性能监控

### 6.1 执行时间监控
```java
/**
 * 性能监控装饰器
 */
public static int computeWithTiming() {
    long startTime = System.currentTimeMillis();
    
    try {
        int result = compute();
        
        long endTime = System.currentTimeMillis();
        debugPrint("算法执行时间: %dms", endTime - startTime);
        
        return result;
    } catch (Exception e) {
        long endTime = System.currentTimeMillis();
        debugPrint("算法执行失败，耗时: %dms", endTime - startTime);
        throw e;
    }
}
```

### 6.2 内存使用监控
```java
/**
 * 内存使用监控
 */
private static void monitorMemoryUsage() {
    if (DEBUG) {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        debugPrint("内存使用: %.2f MB", usedMemory / 1024.0 / 1024.0);
    }
}
```

## 七、测试用例设计

### 7.1 边界测试用例
```java
// 空数组测试
@Test(expected = IllegalArgumentException.class)
public void testEmptyArray() {
    int[] emptyArray = {};
    compute(emptyArray, 1, 2);
}

// 单元素数组测试
@Test
public void testSingleElement() {
    int[] singleArray = {5};
    int result = compute(singleArray, 1, 1);
    assertEquals(5, result);
}

// 极值测试
@Test
public void testExtremeValues() {
    int[] extremeArray = {Integer.MAX_VALUE, Integer.MIN_VALUE};
    // 应该正确处理或抛出适当异常
}
```

### 7.2 性能测试用例
```java
/**
 * 大规模数据性能测试
 */
@Test(timeout = 1000) // 1秒超时
public void testLargeScalePerformance() {
    int n = 100000;
    int[] largeArray = generateLargeArray(n);
    
    // 确保算法在合理时间内完成
    int result = compute(largeArray, 100, 1000);
    assertTrue(result != NA);
}
```

## 八、错误恢复策略

### 8.1 优雅降级
```java
/**
 * 优雅降级：当优化算法失败时使用暴力解法
 */
public static int computeWithFallback() {
    try {
        return computeOptimized();
    } catch (Exception e) {
        debugPrint("优化算法失败，使用暴力解法: %s", e.getMessage());
        return computeBruteForce();
    }
}
```

### 8.2 重试机制
```java
/**
 * 带重试的算法执行
 */
public static int computeWithRetry(int maxRetries) {
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            return compute();
        } catch (Exception e) {
            debugPrint("第%d次尝试失败: %s", attempt, e.getMessage());
            if (attempt == maxRetries) {
                throw e;
            }
            // 可选：添加延迟
            try { Thread.sleep(100); } catch (InterruptedException ie) {}
        }
    }
    throw new IllegalStateException("重试次数用尽");
}
```

## 九、日志记录规范

### 9.1 结构化日志
```java
/**
 * 结构化日志记录
 */
private static void logAlgorithmStep(String step, Map<String, Object> context) {
    if (LOGGER.isDebugEnabled()) {
        StringBuilder logMessage = new StringBuilder(step);
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            logMessage.append(" | ").append(entry.getKey()).append("=").append(entry.getValue());
        }
        LOGGER.debug(logMessage.toString());
    }
}
```

### 9.2 错误日志
```java
/**
 * 错误日志记录
 */
private static void logError(String operation, Exception e, Map<String, Object> context) {
    LOGGER.error("操作失败: {}", operation, e);
    for (Map.Entry<String, Object> entry : context.entrySet()) {
        LOGGER.error("{}: {}", entry.getKey(), entry.getValue());
    }
}
```

## 十、总结

工程化异常处理是确保算法鲁棒性的关键。通过实施上述最佳实践，可以：

1. **提高代码质量**：减少潜在bug
2. **增强用户体验**：提供清晰的错误信息
3. **便于维护调试**：结构化日志和监控
4. **保证系统稳定**：优雅的错误恢复机制

**核心要点：**
- 始终验证输入参数
- 处理所有边界情况
- 监控算法执行状态
- 提供清晰的错误信息
- 实现适当的错误恢复策略