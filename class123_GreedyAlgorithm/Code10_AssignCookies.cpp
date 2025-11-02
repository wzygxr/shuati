// 简化版C++实现，避免使用STL容器
// 由于编译环境问题，使用数组替代vector

// 分发饼干
// 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
// 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
// 并且每块饼干 j，都有一个尺寸 s[j] 。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i，
// 这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
// 测试链接 : https://leetcode.cn/problems/assign-cookies/

/*
 * 贪心算法解法
 * 
 * 核心思想：
 * 1. 为了满足更多的孩子，我们应该优先满足胃口小的孩子（贪心策略）
 * 2. 同时，我们应该优先使用尺寸小的饼干来满足孩子（贪心策略）
 * 3. 这样可以保证尺寸大的饼干留给胃口大的孩子
 * 
 * 算法步骤：
 * 1. 将孩子的胃口值数组g和饼干尺寸数组s分别按升序排序
 * 2. 使用双指针分别指向孩子和饼干
 * 3. 遍历饼干数组，如果当前饼干能满足当前孩子，则两个指针都向前移动
 * 4. 否则只移动饼干指针，尝试用更大的饼干满足当前孩子
 * 
 * 时间复杂度：O(m log m + n log n) - 其中m是孩子数量，n是饼干数量，主要是排序的时间复杂度
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间（不考虑排序的空间复杂度）
 * 
 * 为什么这是最优解？
 * 1. 贪心策略保证了每一步都做出了当前看起来最好的选择
 * 2. 通过数学归纳法可以证明这种策略能得到全局最优解
 * 3. 无法在更少的时间内完成，因为至少需要遍历一遍数组
 * 
 * 工程化考虑：
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 异常处理：输入参数验证
 * 3. 可读性：变量命名清晰，注释详细
 * 
 * 算法调试技巧：
 * 1. 可以通过打印每一步的指针位置来观察匹配过程
 * 2. 用断言验证中间结果是否符合预期
 * 
 * 与机器学习的联系：
 * 1. 贪心策略在机器学习中也有应用，如决策树构建时的信息增益选择
 * 2. 特征选择中也会使用贪心策略选择最优特征子集
 */

int findContentChildren(int g[], int gSize, int s[], int sSize) {
    // 边界条件：如果孩子数组或饼干数组为空，返回0
    if (gSize == 0 || sSize == 0) {
        return 0;
    }

    // 简单排序实现（冒泡排序）
    // 将孩子的胃口值数组按升序排序
    for (int i = 0; i < gSize - 1; i++) {
        for (int j = 0; j < gSize - 1 - i; j++) {
            if (g[j] > g[j + 1]) {
                int temp = g[j];
                g[j] = g[j + 1];
                g[j + 1] = temp;
            }
        }
    }
    
    // 将饼干尺寸数组按升序排序
    for (int i = 0; i < sSize - 1; i++) {
        for (int j = 0; j < sSize - 1 - i; j++) {
            if (s[j] > s[j + 1]) {
                int temp = s[j];
                s[j] = s[j + 1];
                s[j + 1] = temp;
            }
        }
    }

    // childIndex: 指向当前孩子的指针
    int childIndex = 0;
    // cookieIndex: 指向当前饼干的指针
    int cookieIndex = 0;

    // 遍历饼干数组
    while (childIndex < gSize && cookieIndex < sSize) {
        // 如果当前饼干能满足当前孩子
        if (s[cookieIndex] >= g[childIndex]) {
            // 满足的孩子数加1
            childIndex++;
        }
        // 无论是否满足，都要移动饼干指针，尝试下一个饼干
        cookieIndex++;
    }

    return childIndex;
}

// 测试方法
int main() {
    // 测试用例1: g = [1,2,3], s = [1,1] -> 1
    int g1[] = {1, 2, 3};
    int s1[] = {1, 1};
    int g1Size = 3;
    int s1Size = 2;
    // 由于无法使用cout，直接返回结果
    int result1 = findContentChildren(g1, g1Size, s1, s1Size);
    
    // 测试用例2: g = [1,2], s = [1,2,3] -> 2
    int g2[] = {1, 2};
    int s2[] = {1, 2, 3};
    int g2Size = 2;
    int s2Size = 3;
    int result2 = findContentChildren(g2, g2Size, s2, s2Size);
    
    // 测试用例3: g = [1,2,7,8,9], s = [1,3,5,9,10] -> 4
    int g3[] = {1, 2, 7, 8, 9};
    int s3[] = {1, 3, 5, 9, 10};
    int g3Size = 5;
    int s3Size = 5;
    int result3 = findContentChildren(g3, g3Size, s3, s3Size);
    
    // 测试用例4: g = [], s = [1,2,3] -> 0
    int g4[] = {};
    int s4[] = {1, 2, 3};
    int g4Size = 0;
    int s4Size = 3;
    int result4 = findContentChildren(g4, g4Size, s4, s4Size);
    
    // 测试用例5: g = [1,2,3], s = [] -> 0
    int g5[] = {1, 2, 3};
    int s5[] = {};
    int g5Size = 3;
    int s5Size = 0;
    int result5 = findContentChildren(g5, g5Size, s5, s5Size);
    
    // 返回结果（在实际环境中可以通过其他方式输出）
    return 0;
}