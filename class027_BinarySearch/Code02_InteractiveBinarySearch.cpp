/**
 * 交互式二分查找算法实现 (C++版本)
 * 
 * 核心思想：
 * 1. 通过与用户交互来确定目标值的位置
 * 2. 每次询问用户目标值与当前猜测值的关系
 * 3. 根据用户反馈调整搜索范围
 * 
 * 应用场景：
 * 1. 猜数字游戏
 * 2. 交互式问题求解
 * 3. 自适应查询系统
 * 
 * 工程化考量：
 * 1. 用户输入验证
 * 2. 异常处理
 * 3. 查询次数统计
 * 4. 信息论下界计算
 */

// 交互式二分查找函数
int interactiveBinarySearch(int n) {
    int left = 1;
    int right = n;
    int queryCount = 0;
    
    // 说明游戏规则
    // 对于每次猜测，用户需要输入：
    // 1 - 如果猜测的数字比目标小
    // 2 - 如果猜测的数字比目标大
    // 3 - 如果猜测正确
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        queryCount++;
        
        // 输出猜测结果
        // 在实际应用中，这里会与用户交互
        // printf("第%d次猜测：%d\n", queryCount, mid);
        // printf("请输入你的反馈（1/2/3）：");
        
        // 为了演示，我们假设用户输入为3（猜对了）
        int response = 3;
        
        switch (response) {
            case 1:  // 猜的数字比目标小
                left = mid + 1;
                break;
            case 2:  // 猜的数字比目标大
                right = mid - 1;
                break;
            case 3:  // 猜对了
                // printf("太好了！我用了%d次猜测找到了答案：%d\n", queryCount, mid);
                return mid;
            default:
                // printf("输入无效，请输入1、2或3。\n");
                queryCount--;  // 不计入查询次数
                break;
        }
    }
    
    // printf("无法找到答案，请检查你的反馈是否正确。\n");
    return -1;
}

// 计算信息论下界（最小查询次数）
int calculateLowerBound(int n) {
    // 信息论下界：log2(n) 向上取整
    // 简化实现，实际应使用数学库函数
    int result = 0;
    int temp = n;
    while (temp > 1) {
        temp /= 2;
        result++;
    }
    if (n > (1 << result)) {
        result++;
    }
    return result;
}

// 自适应查询优化版本
int adaptiveSearch(int n) {
    int left = 1;
    int right = n;
    int queryCount = 0;
    
    // 计算理论下界
    int lowerBound = calculateLowerBound(n);
    
    while (left <= right) {
        // 自适应选择查询点
        // 简单策略：根据剩余范围的中点选择
        int range = right - left + 1;
        int mid = left + range / 2;
        
        queryCount++;
        
        // 输出猜测结果
        // printf("第%d次猜测：%d\n", queryCount, mid);
        // printf("请输入你的反馈（1/2/3）：");
        
        // 为了演示，我们假设用户输入为3（猜对了）
        int response = 3;
        
        switch (response) {
            case 1:  // 猜的数字比目标小
                left = mid + 1;
                break;
            case 2:  // 猜的数字比目标大
                right = mid - 1;
                break;
            case 3:  // 猜对了
                // printf("太好了！我用了%d次猜测找到了答案：%d\n", queryCount, mid);
                // printf("查询效率：%.2f倍理论下界\n", (double) queryCount / lowerBound);
                return mid;
            default:
                // printf("输入无效，请输入1、2或3。\n");
                queryCount--;  // 不计入查询次数
                break;
        }
    }
    
    // printf("无法找到答案，请检查你的反馈是否正确。\n");
    return -1;
}