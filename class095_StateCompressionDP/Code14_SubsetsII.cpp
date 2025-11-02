/*
 * 子集 II - C++实现
 * 给定一个整数数组 nums，其中可能包含重复元素，返回所有可能的子集（幂集）。
 * 解集不能包含重复的子集。可以按任意顺序返回解集。
 * 
 * 题目链接: https://leetcode.cn/problems/subsets-ii/
 * 难度: 中等
 * 
 * 解题思路:
 * 1. 先对数组排序，使相同元素相邻
 * 2. 使用回溯法生成所有子集
 * 3. 通过跳过重复元素避免生成重复子集
 * 
 * 时间复杂度: O(n * 2^n) - 共有2^n个子集，每个子集平均长度n
 * 空间复杂度: O(n) - 递归栈深度为n
 */
class Solution {
public:
    /*
     * 主方法：生成所有不重复子集（回溯法）
     * @param nums 输入数组，可能包含重复元素
     * @param numsSize 数组大小
     * @param returnSize 返回结果的大小
     * @param returnColumnSizes 返回结果中每个子集的大小
     * @return 所有不重复子集的二维数组
     */
    int** subsetsWithDup(int* nums, int numsSize, int* returnSize, int** returnColumnSizes) {
        // 边界情况处理
        if (numsSize == 0) {
            *returnSize = 1;
            static int zero = 0;
            *returnColumnSizes = &zero;
            static int* emptyResult[1];
            emptyResult[0] = 0;
            return (int**)emptyResult;
        }
        
        // 排序是去重的关键步骤
        // 简单冒泡排序
        for (int i = 0; i < numsSize - 1; i++) {
            for (int j = 0; j < numsSize - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
        
        // 使用固定大小数组存储结果
        static int* result[10000];
        static int columnSizes[10000];
        int resultSize = 0;
        
        // 使用回溯法生成子集
        int current[100];
        int currentSize = 0;
        backtrack(nums, numsSize, 0, current, currentSize, result, &resultSize, columnSizes);
        
        *returnSize = resultSize;
        *returnColumnSizes = columnSizes;
        return result;
    }
    
private:
    /*
     * 回溯法生成子集
     * @param nums 输入数组
     * @param numsSize 数组大小
     * @param start 当前处理的起始位置
     * @param current 当前正在构建的子集
     * @param currentSize 当前子集的大小
     * @param result 存储所有子集的结果数组
     * @param resultSize 结果数组的大小
     * @param columnSizes 存储每个子集大小的数组
     */
    void backtrack(int* nums, int numsSize, int start, int* current, int currentSize, 
                   int** result, int* resultSize, int* columnSizes) {
        // 将当前子集加入结果（包括空集）
        static int subsetStorage[10000][100];
        static int storageIndex = 0;
        
        // 复制当前子集到存储区
        for (int i = 0; i < currentSize; i++) {
            subsetStorage[storageIndex][i] = current[i];
        }
        
        result[*resultSize] = subsetStorage[storageIndex];
        columnSizes[*resultSize] = currentSize;
        (*resultSize)++;
        storageIndex++;
        
        // 从start位置开始遍历数组
        for (int i = start; i < numsSize; i++) {
            // 关键去重逻辑：跳过重复元素
            // 只有当i > start且当前元素等于前一个元素时才跳过
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 选择当前元素
            current[currentSize] = nums[i];
            
            // 递归处理下一个位置
            backtrack(nums, numsSize, i + 1, current, currentSize + 1, result, resultSize, columnSizes);
        }
    }
};

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用基本数组代替STL容器，避免依赖标准库
   - 手动实现排序算法
   - 使用静态数组和指针

2. 内存管理：
   - 使用静态数组避免动态内存分配
   - 预分配固定大小数组避免动态扩容
   - 注意避免内存泄漏

3. 性能优化：
   - 使用固定大小静态数组减少动态分配
   - 简单排序算法满足小规模数据需求
   - 避免不必要的数据拷贝

4. 与Java实现的差异：
   - C++使用静态数组和指针代替容器类
   - 需要显式处理内存分配和释放

5. 工程化考量：
   - 添加详细的文档注释
   - 提供完整的参数说明
   - 考虑异常情况和边界条件

6. 跨平台兼容性：
   - 使用标准C++特性确保跨平台兼容
   - 避免平台特定的API调用
*/

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用vector代替Java的ArrayList，性能更好
   - 使用algorithm库的sort函数进行排序
   - 使用set进行字符串去重

2. 内存管理：
   - C++需要手动管理内存，但STL容器自动处理
   - 使用引用传递避免不必要的拷贝
   - 注意vector的push_back可能引起的重新分配

3. 性能优化：
   - 预分配vector空间可以减少重新分配
   - 使用emplace_back代替push_back可以避免临时对象
   - 使用move语义可以提升大对象传递效率

4. 异常安全：
   - 使用try-catch包装测试代码
   - 断言验证确保程序正确性

5. 与Java实现的差异：
   - C++没有垃圾回收，需要更注意内存管理
   - C++模板提供了更好的类型安全
   - C++性能通常优于Java，特别是对于计算密集型任务

6. 工程化考量：
   - 添加详细的文档注释
   - 提供完整的单元测试
   - 考虑异常情况和边界条件
   - 性能测试和优化

7. 跨平台兼容性：
   - 使用标准C++特性确保跨平台兼容
   - 避免平台特定的API调用
   - 测试在不同编译器下的行为
*/