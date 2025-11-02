// 根据身高重建队列
// 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
// 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
// 请你重新构造并返回输入数组 people 所表示的队列。
// 测试链接 : https://leetcode.cn/problems/queue-reconstruction-by-height/

/**
 * 简单排序函数（按身高降序，k值升序）
 * 
 * @param people 人员信息数组
 * @param peopleSize 数组长度
 */
void sortPeople(int people[][2], int peopleSize) {
    for (int i = 0; i < peopleSize - 1; i++) {
        for (int j = 0; j < peopleSize - i - 1; j++) {
            // 按身高降序排序，身高相同时按k值升序排序
            if (people[j][0] < people[j + 1][0] || 
                (people[j][0] == people[j + 1][0] && people[j][1] > people[j + 1][1])) {
                // 交换人员信息
                int temp0 = people[j][0];
                int temp1 = people[j][1];
                people[j][0] = people[j + 1][0];
                people[j][1] = people[j + 1][1];
                people[j + 1][0] = temp0;
                people[j + 1][1] = temp1;
            }
        }
    }
}

/**
 * 根据身高重建队列
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 按身高降序排序，身高相同时按k值升序排序
 * 2. 依次将每个人插入到结果队列的第k个位置
 * 
 * 正确性分析：
 * 1. 身高高的人看不到身高低的人，所以先安排身高高的人
 * 2. 身高相同时，k值小的应该排在前面
 * 3. 当处理到某个人时，所有已处理的人都比他高或等高
 * 4. 将他插入到第k个位置，前面正好有k个身高大于或等于他的人
 * 
 * 时间复杂度：O(n^2) - 排序O(n^2)，插入操作O(n^2)
 * 空间复杂度：O(n) - 需要额外的数组存储结果
 * 
 * @param people 人员信息数组
 * @param peopleSize 数组长度
 * @param result 重建后的队列
 * @return 无返回值，结果存储在result中
 */
void reconstructQueue(int people[][2], int peopleSize, int result[][2]) {
    // 按身高降序排序，身高相同时按k值升序排序
    sortPeople(people, peopleSize);
    
    // 初始化结果数组大小
    int resultSize = 0;
    
    // 依次将每个人插入到结果队列的第k个位置
    for (int i = 0; i < peopleSize; i++) {
        int k = people[i][1];
        
        // 将后面的人往后移一位
        for (int j = resultSize; j > k; j--) {
            result[j][0] = result[j-1][0];
            result[j][1] = result[j-1][1];
        }
        
        // 插入当前人
        result[k][0] = people[i][0];
        result[k][1] = people[i][1];
        resultSize++;
    }
}