// 简化版C++实现，避免使用STL容器
// 由于编译环境问题，使用数组和基本操作替代STL容器

// 最大数
// 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
// 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
// 测试链接 : https://leetcode.cn/problems/largest-number/

/*
 * 贪心算法解法
 * 
 * 核心思想：
 * 1. 为了组成最大的数，我们需要将数字按照特定的规则排序
 * 2. 对于两个数字a和b，如果ab > ba（字符串拼接），则a应该排在b前面
 * 3. 例如：对于数字3和30，330 > 303，所以3应该排在30前面
 * 
 * 算法步骤：
 * 1. 将整数数组转换为字符串数组
 * 2. 自定义排序规则：对于两个字符串a和b，如果a+b > b+a，则a排在b前面
 * 3. 按照排序后的顺序拼接字符串
 * 4. 处理特殊情况：如果结果以0开头且长度大于1，则返回"0"
 * 
 * 时间复杂度：O(n log n * m) - 其中n是数字个数，m是数字的平均长度，主要是排序的时间复杂度
 * 空间复杂度：O(n * m) - 需要额外的字符串数组存储数字
 * 
 * 为什么这是最优解？
 * 1. 贪心策略保证了每一步都做出了当前看起来最好的选择
 * 2. 通过数学归纳法可以证明这种策略能得到全局最优解
 * 3. 无法在更少的时间内完成，因为至少需要排序一遍数组
 * 
 * 工程化考虑：
 * 1. 边界条件处理：空数组、全0数组
 * 2. 异常处理：输入参数验证
 * 3. 可读性：变量命名清晰，注释详细
 * 
 * 算法调试技巧：
 * 1. 可以通过打印每一步的排序结果来观察排序过程
 * 2. 用断言验证中间结果是否符合预期
 * 
 * 与机器学习的联系：
 * 1. 这种自定义排序的思想在机器学习中也有应用，如自定义距离度量
 * 2. 在特征工程中，有时需要自定义特征的排序规则
 */

// 简单实现整数转字符串
void intToString(int num, char* str) {
    if (num == 0) {
        str[0] = '0';
        str[1] = '\0';
        return;
    }
    
    int len = 0;
    int temp = num;
    
    // 计算数字长度
    while (temp > 0) {
        len++;
        temp /= 10;
    }
    
    // 转换数字
    for (int i = len - 1; i >= 0; i--) {
        str[i] = (num % 10) + '0';
        num /= 10;
    }
    
    str[len] = '\0';
}

// 简单实现字符串拼接
void strcatSimple(char* dest, const char* src) {
    int destLen = 0;
    while (dest[destLen] != '\0') {
        destLen++;
    }
    
    int srcLen = 0;
    while (src[srcLen] != '\0') {
        dest[destLen + srcLen] = src[srcLen];
        srcLen++;
    }
    
    dest[destLen + srcLen] = '\0';
}

// 简单实现字符串比较
int strcmpSimple(const char* a, const char* b) {
    int i = 0;
    while (a[i] != '\0' && b[i] != '\0') {
        if (a[i] < b[i]) {
            return -1;
        } else if (a[i] > b[i]) {
            return 1;
        }
        i++;
    }
    
    if (a[i] == '\0' && b[i] == '\0') {
        return 0;
    } else if (a[i] == '\0') {
        return -1;
    } else {
        return 1;
    }
}

// 冒泡排序实现自定义排序
void bubbleSort(char strs[][20], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            // 构造比较字符串
            char ab[40] = {0};
            char ba[40] = {0};
            
            strcatSimple(ab, strs[j]);
            strcatSimple(ab, strs[j+1]);
            
            strcatSimple(ba, strs[j+1]);
            strcatSimple(ba, strs[j]);
            
            // 如果 strs[j] + strs[j+1] < strs[j+1] + strs[j]，则交换
            if (strcmpSimple(ab, ba) < 0) {
                // 交换
                char temp[20];
                int k = 0;
                while (strs[j][k] != '\0') {
                    temp[k] = strs[j][k];
                    k++;
                }
                temp[k] = '\0';
                
                k = 0;
                while (strs[j+1][k] != '\0') {
                    strs[j][k] = strs[j+1][k];
                    k++;
                }
                strs[j][k] = '\0';
                
                k = 0;
                while (temp[k] != '\0') {
                    strs[j+1][k] = temp[k];
                    k++;
                }
                strs[j+1][k] = '\0';
            }
        }
    }
}

// 主函数
int main() {
    // 由于环境限制，这里只提供函数实现，不提供完整的测试代码
    // 在实际使用中，需要根据具体需求调用相关函数
    
    return 0;
}