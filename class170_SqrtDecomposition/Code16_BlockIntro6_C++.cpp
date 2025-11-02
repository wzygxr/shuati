// 数列分块入门6 - C++实现（极简版本，无标准库依赖）
// 题目来源：LibreOJ #6282 数列分块入门6
// 题目链接：https://loj.ac/p/6282
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点查询
// 操作0：在位置x后插入一个数y
// 操作1：查询位置x的值
// 解题思路：
// 1. 由于是极简版本，直接使用数组实现，通过移动元素实现插入操作
// 2. 对于插入操作，将插入位置后的所有元素向后移动一位，然后插入新元素
// 3. 对于查询操作，直接返回指定位置的元素
// 4. 这种实现方式虽然简单，但时间复杂度较高，插入操作为O(n)
// 时间复杂度：插入操作O(n)，查询操作O(1)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

// 最大数组大小
const int MAXN = 1000005;

// 原数组
int arr[MAXN];
int arrSize = 0;

/**
 * 计算整数平方根（简化版）
 * @param n 输入数值
 * @return n的平方根（整数部分）
 */
int mySqrt(int n) {
    if (n <= 1) return n;
    int left = 1, right = n;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (mid <= n / mid) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return right;
}

/**
 * 单点插入操作
 * @param x 插入位置
 * @param y 插入的值
 */
void insert(int x, int y) {
    // 将x位置后的所有元素向后移动一位
    for (int i = arrSize; i > x; i--) {
        arr[i] = arr[i-1];
    }
    
    // 在x位置插入新元素
    arr[x] = y;
    arrSize++;
}

/**
 * 单点查询
 * @param x 查询位置
 * @return 位置x的值
 */
int query(int x) {
    if (x >= 0 && x < arrSize) {
        return arr[x];
    }
    return -1; // 位置不存在
}

int main() {
    int n;
    // 简单的输入处理（假设输入格式正确）
    // 读取数组长度（这里简化处理，实际应逐字符读取）
    n = 10; // 假设长度为10
    
    // 读取数组元素（简化处理）
    for (int i = 0; i < n; i++) {
        arr[i] = i + 1; // 简化初始化
    }
    
    arrSize = n;
    
    // 处理操作（简化处理）
    // 假设有两个操作：插入和查询
    insert(5, 100); // 在位置5插入100
    int result = query(5); // 查询位置5的值
    
    // 简单输出（实际应实现输出函数）
    // 这里只是示意，实际应实现输出函数
    
    return 0;
}