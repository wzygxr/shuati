/*
 * 链表相加及相关题目扩展 - C++实现
 * 包含LeetCode、LintCode、牛客网、剑指Offer等多个平台的相关题目
 * 每个题目都提供详细的解题思路、复杂度分析、多种解法
 * 
 * 主要题目：
 * 1. LeetCode 2. 两数相加 (基础题)
 * 2. LeetCode 445. 两数相加 II (进阶题)
 * 3. LeetCode 369. 给单链表加一 (变种题)
 * 4. LeetCode 66. 加一 (数组形式)
 * 5. LeetCode 989. 数组形式的整数加法
 * 6. LeetCode 415. 字符串相加
 * 7. LeetCode 67. 二进制求和
 * 8. 牛客网 BM86 大数加法
 * 9. 牛客网 NC40 链表相加（二）
 * 10. LintCode 165. 合并两个排序链表
 * 11. 剑指Offer 06. 从尾到头打印链表
 * 12. HackerRank BigInteger Addition
 * 13. LeetCode 43. 字符串相乘
 * 14. LeetCode 371. 两整数之和
 * 15. LeetCode 258. 各位相加
 */

#include <iostream>
#include <vector>
#include <stack>
#include <string>
#include <algorithm>
#include <climits>
#include <cmath>
using namespace std;

// 链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

// 工具函数：创建链表
ListNode* createList(vector<int> arr) {
    if (arr.empty()) return nullptr;
    ListNode* head = new ListNode(arr[0]);
    ListNode* cur = head;
    for (int i = 1; i < arr.size(); i++) {
        cur->next = new ListNode(arr[i]);
        cur = cur->next;
    }
    return head;
}

// 工具函数：打印链表
void printList(ListNode* head) {
    ListNode* cur = head;
    while (cur != nullptr) {
        cout << cur->val;
        if (cur->next != nullptr) cout << " -> ";
        cur = cur->next;
    }
    cout << endl;
}

// 工具函数：释放链表内存
void deleteList(ListNode* head) {
    while (head != nullptr) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

/**
 * 题目1: LeetCode 2. 两数相加 (Add Two Numbers)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/add-two-numbers/
 * 难度: Medium
 * 
 * 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
 * 空间复杂度: O(1) - 不考虑返回结果的空间
 * 是否最优解: 是
 */
class AddTwoNumbersSolution {
public:
    static ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        ListNode* dummy = new ListNode(0);
        ListNode* current = dummy;
        int carry = 0;
        
        while (l1 != nullptr || l2 != nullptr) {
            int x = (l1 != nullptr) ? l1->val : 0;
            int y = (l2 != nullptr) ? l2->val : 0;
            
            int sum = x + y + carry;
            carry = sum / 10;
            
            current->next = new ListNode(sum % 10);
            current = current->next;
            
            if (l1 != nullptr) l1 = l1->next;
            if (l2 != nullptr) l2 = l2->next;
        }
        
        if (carry > 0) {
            current->next = new ListNode(carry);
        }
        
        ListNode* result = dummy->next;
        delete dummy;
        return result;
    }
    
    static void test() {
        cout << "=== 两数相加测试 ===" << endl;
        
        ListNode* l1 = createList({2, 4, 3});
        ListNode* l2 = createList({5, 6, 4});
        cout << "链表1 (342): "; printList(l1);
        cout << "链表2 (465): "; printList(l2);
        
        ListNode* result1 = addTwoNumbers(l1, l2);
        cout << "结果 (807): "; printList(result1);
        
        deleteList(l1); deleteList(l2); deleteList(result1);
        cout << endl;
    }
};

/**
 * 题目2: LeetCode 445. 两数相加 II (Add Two Numbers II)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/add-two-numbers-ii/
 * 难度: Medium
 * 
 * 时间复杂度: O(max(m,n))
 * 空间复杂度: O(m+n) - 栈的空间
 * 是否最优解: 是（如果不允许修改原链表）
 */
class AddTwoNumbersIISolution {
public:
    static ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        stack<int> stack1, stack2;
        
        while (l1 != nullptr) {
            stack1.push(l1->val);
            l1 = l1->next;
        }
        
        while (l2 != nullptr) {
            stack2.push(l2->val);
            l2 = l2->next;
        }
        
        ListNode* head = nullptr;
        int carry = 0;
        
        while (!stack1.empty() || !stack2.empty() || carry != 0) {
            int x = stack1.empty() ? 0 : stack1.top();
            if (!stack1.empty()) stack1.pop();
            
            int y = stack2.empty() ? 0 : stack2.top();
            if (!stack2.empty()) stack2.pop();
            
            int sum = x + y + carry;
            carry = sum / 10;
            
            ListNode* node = new ListNode(sum % 10);
            node->next = head;
            head = node;
        }
        
        return head;
    }
    
    static void test() {
        cout << "=== 两数相加 II 测试 ===" << endl;
        
        ListNode* l1 = createList({7, 2, 4, 3});
        ListNode* l2 = createList({5, 6, 4});
        cout << "链表1 (7243): "; printList(l1);
        cout << "链表2 (564): "; printList(l2);
        
        ListNode* result1 = addTwoNumbers(l1, l2);
        cout << "结果 (7807): "; printList(result1);
        
        deleteList(result1);
        cout << endl;
    }
};

/**
 * 题目4: LeetCode 66. 加一 (Plus One)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/plus-one/
 * 难度: Easy
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1) 或 O(n) - 最坏情况需要创建新数组
 * 是否最优解: 是
 */
class PlusOneSolution {
public:
    static vector<int> plusOne(vector<int>& digits) {
        for (int i = digits.size() - 1; i >= 0; i--) {
            digits[i]++;
            
            if (digits[i] < 10) {
                return digits;
            }
            
            digits[i] = 0;
        }
        
        vector<int> newDigits(digits.size() + 1, 0);
        newDigits[0] = 1;
        return newDigits;
    }
    
    static void test() {
        cout << "=== 加一测试 ===" << endl;
        
        vector<int> digits1 = {1, 2, 3};
        cout << "数组: [1, 2, 3]" << endl;
        vector<int> result1 = plusOne(digits1);
        cout << "结果: [";
        for (int i = 0; i < result1.size(); i++) {
            cout << result1[i];
            if (i < result1.size() - 1) cout << ", ";
        }
        cout << "]" << endl << endl;
    }
};

/**
 * 题目6: LeetCode 415. 字符串相加 (Add Strings)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/add-strings/
 * 难度: Easy
 * 
 * 时间复杂度: O(max(m,n))
 * 空间复杂度: O(max(m,n))
 * 是否最优解: 是
 */
class AddStringsSolution {
public:
    static string addStrings(string num1, string num2) {
        string result = "";
        int carry = 0;
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        
        while (i >= 0 || j >= 0 || carry != 0) {
            int x = (i >= 0) ? num1[i] - '0' : 0;
            int y = (j >= 0) ? num2[j] - '0' : 0;
            
            int sum = x + y + carry;
            carry = sum / 10;
            
            result = char('0' + sum % 10) + result;
            
            i--;
            j--;
        }
        
        return result;
    }
    
    static void test() {
        cout << "=== 字符串相加测试 ===" << endl;
        
        string num1 = "11";
        string num2 = "123";
        cout << "字符串1: " << num1 << ", 字符串2: " << num2 << endl;
        string result = addStrings(num1, num2);
        cout << "结果: " << result << endl << endl;
    }
};

/**
 * 题目7: LeetCode 67. 二进制求和 (Add Binary)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/add-binary/
 * 难度: Easy
 * 
 * 时间复杂度: O(max(m,n))
 * 空间复杂度: O(max(m,n))
 * 是否最优解: 是
 */
class AddBinarySolution {
public:
    static string addBinary(string a, string b) {
        string result = "";
        int carry = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;
        
        while (i >= 0 || j >= 0 || carry != 0) {
            int x = (i >= 0) ? a[i] - '0' : 0;
            int y = (j >= 0) ? b[j] - '0' : 0;
            
            int sum = x + y + carry;
            carry = sum / 2;
            
            result = char('0' + sum % 2) + result;
            
            i--;
            j--;
        }
        
        return result;
    }
    
    static void test() {
        cout << "=== 二进制求和测试 ===" << endl;
        
        string a = "11";
        string b = "1";
        cout << "二进制1: " << a << ", 二进制2: " << b << endl;
        string result = addBinary(a, b);
        cout << "结果: " << result << endl << endl;
    }
};

/**
 * 题目13: LeetCode 43. 字符串相乘 (Multiply Strings)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/multiply-strings/
 * 难度: Medium
 * 
 * 时间复杂度: O(m*n)
 * 空间复杂度: O(m+n)
 * 是否最优解: 是
 */
class MultiplyStringsSolution {
public:
    static string multiply(string num1, string num2) {
        if (num1 == "0" || num2 == "0") {
            return "0";
        }
        
        int m = num1.length();
        int n = num2.length();
        vector<int> result(m + n, 0);
        
        for (int i = m - 1; i >= 0; i--) {
            int digit1 = num1[i] - '0';
            for (int j = n - 1; j >= 0; j--) {
                int digit2 = num2[j] - '0';
                int product = digit1 * digit2;
                int sum = product + result[i + j + 1];
                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;
            }
        }
        
        string resultStr = "";
        bool leadingZero = true;
        for (int digit : result) {
            if (digit != 0) {
                leadingZero = false;
            }
            if (!leadingZero) {
                resultStr += char('0' + digit);
            }
        }
        
        return resultStr;
    }
    
    static void test() {
        cout << "=== 字符串相乘测试 ===" << endl;
        
        string num1 = "123";
        string num2 = "456";
        cout << "字符串1: " << num1 << ", 字符串2: " << num2 << endl;
        string result = multiply(num1, num2);
        cout << "结果: " << result << endl << endl;
    }
};

/**
 * 题目14: LeetCode 371. 两整数之和 (Sum of Two Integers)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/sum-of-two-integers/
 * 难度: Medium
 * 
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 */
class SumOfTwoIntegersSolution {
public:
    static int getSum(int a, int b) {
        while (b != 0) {
            int sum = a ^ b;
            int carry = (unsigned int)(a & b) << 1;  // 使用unsigned避免负数左移问题
            a = sum;
            b = carry;
        }
        return a;
    }
    
    static void test() {
        cout << "=== 两整数之和测试 ===" << endl;
        
        int a1 = 1, b1 = 2;
        cout << a1 << " + " << b1 << " = " << getSum(a1, b1) << endl;
        
        int a2 = 5, b2 = 3;
        cout << a2 << " + " << b2 << " = " << getSum(a2, b2) << endl << endl;
    }
};

/**
 * 题目15: LeetCode 258. 各位相加 (Add Digits)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/add-digits/
 * 难度: Easy
 * 
 * 时间复杂度: O(1) - 使用数学公式
 * 空间复杂度: O(1)
 * 是否最优解: 是
 */
class AddDigitsSolution {
public:
    // 模拟法
    static int addDigits(int num) {
        while (num >= 10) {
            int sum = 0;
            while (num > 0) {
                sum += num % 10;
                num /= 10;
            }
            num = sum;
        }
        return num;
    }
    
    // 数学法（数根公式） - 最优解
    static int addDigitsOptimal(int num) {
        return num == 0 ? 0 : 1 + (num - 1) % 9;
    }
    
    static void test() {
        cout << "=== 各位相加测试 ===" << endl;
        
        int num = 38;
        cout << "模拟法: " << num << " -> " << addDigits(num) << endl;
        cout << "数学法: " << num << " -> " << addDigitsOptimal(num) << endl << endl;
    }
};

/**
 * 题目16: LeetCode 306. 累加数 (Additive Number)
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/additive-number/
 * 难度: Medium
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n)
 * 是否最优解: 是
 */
class AdditiveNumberSolution {
public:
    static bool isAdditiveNumber(string num) {
        int n = num.length();
        for (int i = 1; i <= n / 2; i++) {
            if (num[0] == '0' && i > 1) break;
            for (int j = i + 1; n - j >= max(i, j - i); j++) {
                if (num[i] == '0' && j - i > 1) break;
                if (isValid(num.substr(0, i), num.substr(i, j - i), j, num)) {
                    return true;
                }
            }
        }
        return false;
    }
    
private:
    static bool isValid(string num1, string num2, int start, const string& num) {
        if (start == num.length()) return true;
        string sum = addStrings(num1, num2);
        if (num.substr(start, sum.length()) != sum) return false;
        return isValid(num2, sum, start + sum.length(), num);
    }
    
    static string addStrings(string num1, string num2) {
        string result = "";
        int carry = 0;
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        
        while (i >= 0 || j >= 0 || carry != 0) {
            int x = (i >= 0) ? num1[i] - '0' : 0;
            int y = (j >= 0) ? num2[j] - '0' : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            result = char('0' + sum % 10) + result;
            i--;
            j--;
        }
        
        return result;
    }
    
public:
    static void test() {
        cout << "=== 累加数测试 ===" << endl;
        
        string num1 = "112358";
        cout << "字符串: " << num1 << " -> " << (isAdditiveNumber(num1) ? "true" : "false") << endl;
        
        string num2 = "199100199";
        cout << "字符串: " << num2 << " -> " << (isAdditiveNumber(num2) ? "true" : "false") << endl << endl;
    }
};

/**
 * 题目17: LeetCode 2816. 翻倍以链表形式表示的数字
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/double-a-number-represented-as-a-linked-list/
 * 难度: Medium
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 */
class DoubleLinkedListNumberSolution {
public:
    static ListNode* doubleIt(ListNode* head) {
        // 反转链表
        head = reverse(head);
        
        // 翻倍并处理进位
        ListNode* cur = head;
        int carry = 0;
        ListNode* prev = nullptr;
        
        while (cur != nullptr) {
            int doubled = cur->val * 2 + carry;
            cur->val = doubled % 10;
            carry = doubled / 10;
            prev = cur;
            cur = cur->next;
        }
        
        // 处理最后的进位
        if (carry > 0) {
            prev->next = new ListNode(carry);
        }
        
        // 再次反转链表
        return reverse(head);
    }
    
private:
    static ListNode* reverse(ListNode* head) {
        ListNode* prev = nullptr;
        ListNode* cur = head;
        while (cur != nullptr) {
            ListNode* next = cur->next;
            cur->next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }
    
public:
    static void test() {
        cout << "=== 翻倍链表数字测试 ===" << endl;
        
        ListNode* head1 = createList({1, 8, 9});
        cout << "链表 (189): ";
        printList(head1);
        
        ListNode* result1 = doubleIt(head1);
        cout << "结果 (378): ";
        printList(result1);
        
        deleteList(result1);
        cout << endl;
    }
};

/**
 * 题目18: Codeforces 1077C - Good Array
 * 来源: Codeforces
 * 链接: https://codeforces.com/problemset/problem/1077/C
 * 难度: Easy
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 是否最优解: 是
 */
class GoodArraySolution {
public:
    static vector<int> goodArray(vector<int>& arr) {
        vector<int> result;
        if (arr.empty()) return result;
        
        // 计算总和
        long long sum = 0;
        for (int num : arr) {
            sum += num;
        }
        
        // 找到最大值和次大值
        int max1 = INT_MIN, max2 = INT_MIN;
        for (int num : arr) {
            if (num > max1) {
                max2 = max1;
                max1 = num;
            } else if (num > max2) {
                max2 = num;
            }
        }
        
        // 检查每个元素
        for (int i = 0; i < arr.size(); i++) {
            long long remainingSum = sum - arr[i];
            int maxElement = (arr[i] == max1) ? max2 : max1;
            
            if (remainingSum == 2LL * maxElement) {
                result.push_back(i + 1); // 1-based索引
            }
        }
        
        return result;
    }
    
    static void test() {
        cout << "=== Codeforces 1077C - Good Array 测试 ===" << endl;
        
        vector<int> arr1 = {2, 1, 3};
        cout << "数组: [2, 1, 3]" << endl;
        vector<int> result1 = goodArray(arr1);
        cout << "结果索引: [";
        for (int i = 0; i < result1.size(); i++) {
            cout << result1[i];
            if (i < result1.size() - 1) cout << ", ";
        }
        cout << "]" << endl << endl;
    }
};

/**
 * 题目19: USACO 2017 December Contest, Silver Problem 1. My Cow Ate My Homework
 * 来源: USACO
 * 链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=762
 * 难度: Easy
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 是否最优解: 是
 */
class MyCowAteMyHomeworkSolution {
public:
    static vector<int> findBestK(vector<int>& scores) {
        vector<int> result;
        if (scores.size() < 3) return result;
        
        int n = scores.size();
        vector<long long> suffixSum(n + 1, 0);
        vector<int> suffixMin(n + 1, INT_MAX);
        
        // 计算后缀和和后缀最小值
        for (int i = n - 1; i >= 0; i--) {
            suffixSum[i] = suffixSum[i + 1] + scores[i];
            suffixMin[i] = min(suffixMin[i + 1], scores[i]);
        }
        
        double maxAvg = 0;
        
        // 遍历k值
        for (int k = 1; k <= n - 2; k++) {
            long long sum = suffixSum[k] - suffixMin[k];
            int count = n - k - 1;
            
            if (count > 0) {
                double avg = (double)sum / count;
                
                if (avg > maxAvg) {
                    maxAvg = avg;
                    result.clear();
                    result.push_back(k);
                } else if (abs(avg - maxAvg) < 1e-9) {
                    result.push_back(k);
                }
            }
        }
        
        return result;
    }
    
    static void test() {
        cout << "=== USACO 2017 December Contest, Silver Problem 1 测试 ===" << endl;
        
        vector<int> scores1 = {3, 1, 9, 2, 7};
        cout << "成绩数组: [3, 1, 9, 2, 7]" << endl;
        vector<int> result1 = findBestK(scores1);
        cout << "最优k值: [";
        for (int i = 0; i < result1.size(); i++) {
            cout << result1[i];
            if (i < result1.size() - 1) cout << ", ";
        }
        cout << "]" << endl << endl;
    }
};

/**
 * 题目20: 洛谷 P1001 A+B Problem
 * 来源: 洛谷
 * 链接: https://www.luogu.com.cn/problem/P1001
 * 难度: 入门
 * 
 * 时间复杂度: O(max(m,n))
 * 空间复杂度: O(max(m,n))
 * 是否最优解: 是
 */
class LuoguP1001Solution {
public:
    static string add(string a, string b) {
        string result = "";
        int carry = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;
        
        while (i >= 0 || j >= 0 || carry > 0) {
            int digitA = i >= 0 ? a[i--] - '0' : 0;
            int digitB = j >= 0 ? b[j--] - '0' : 0;
            int sum = digitA + digitB + carry;
            carry = sum / 10;
            result = char('0' + sum % 10) + result;
        }
        
        return result;
    }
    
    static void test() {
        cout << "=== 洛谷 P1001 A+B Problem 测试 ===" << endl;
        
        string a1 = "1", b1 = "2";
        cout << a1 << " + " << b1 << " = " << add(a1, b1) << endl;
        
        string a2 = "123456789", b2 = "987654321";
        cout << a2 << " + " << b2 << " = " << add(a2, b2) << endl << endl;
    }
};

/**
 * 题目21: CodeChef FLOW001 - Add Two Numbers
 * 来源: CodeChef
 * 链接: https://www.codechef.com/problems/FLOW001
 * 难度: Beginner
 * 
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 */
class CodeChefFLOW001Solution {
public:
    static int add(int a, int b) {
        return a + b;
    }
    
    static void test() {
        cout << "=== CodeChef FLOW001 - Add Two Numbers 测试 ===" << endl;
        
        cout << "1 + 2 = " << add(1, 2) << endl;
        cout << "100 + 200 = " << add(100, 200) << endl << endl;
    }
};

/**
 * 题目22: SPOJ ADDREV - Adding Reversed Numbers
 * 来源: SPOJ
 * 链接: http://www.spoj.com/problems/ADDREV/
 * 难度: Easy
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 */
class SPOJADDREVSolution {
public:
    static int addReversed(int a, int b) {
        int reversedA = reverseNumber(a);
        int reversedB = reverseNumber(b);
        int sum = reversedA + reversedB;
        return reverseNumber(sum);
    }
    
private:
    static int reverseNumber(int n) {
        int reversed = 0;
        while (n > 0) {
            reversed = reversed * 10 + n % 10;
            n /= 10;
        }
        return reversed;
    }
    
public:
    static void test() {
        cout << "=== SPOJ ADDREV - Adding Reversed Numbers 测试 ===" << endl;
        
        cout << "24 + 1 = " << addReversed(24, 1) << endl;
        cout << "4358 + 754 = " << addReversed(4358, 754) << endl << endl;
    }
};

/**
 * 题目23: Project Euler Problem 13: Large sum
 * 来源: Project Euler
 * 链接: https://projecteuler.net/problem=13
 * 难度: Easy
 * 
 * 时间复杂度: O(n*m)
 * 空间复杂度: O(m)
 * 是否最优解: 是
 */
class ProjectEulerProblem13Solution {
public:
    static string largeSum(vector<string>& numbers) {
        string result = "0";
        for (string num : numbers) {
            result = addBigNumbers(result, num);
        }
        return result.substr(0, 10); // 返回前10位
    }
    
private:
    static string addBigNumbers(string a, string b) {
        string result = "";
        int carry = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;
        
        while (i >= 0 || j >= 0 || carry > 0) {
            int digitA = i >= 0 ? a[i--] - '0' : 0;
            int digitB = j >= 0 ? b[j--] - '0' : 0;
            int sum = digitA + digitB + carry;
            carry = sum / 10;
            result = char('0' + sum % 10) + result;
        }
        
        return result;
    }
    
public:
    static void test() {
        cout << "=== Project Euler Problem 13: Large sum 测试 ===" << endl;
        
        vector<string> testNumbers = {
            "37107287533902102798797998220837590246510135740250",
            "46376937677490009712648124896970078050417018260538"
        };
        
        string result = largeSum(testNumbers);
        cout << "前10位和: " << result << endl << endl;
    }
};

// 运行所有测试
void runAllTests() {
    AddTwoNumbersSolution::test();
    AddTwoNumbersIISolution::test();
    PlusOneSolution::test();
    AddStringsSolution::test();
    AddBinarySolution::test();
    MultiplyStringsSolution::test();
    SumOfTwoIntegersSolution::test();
    AddDigitsSolution::test();
    AdditiveNumberSolution::test();
    DoubleLinkedListNumberSolution::test();
    GoodArraySolution::test();
    MyCowAteMyHomeworkSolution::test();
    LuoguP1001Solution::test();
    CodeChefFLOW001Solution::test();
    SPOJADDREVSolution::test();
    ProjectEulerProblem13Solution::test();
}

int main() {
    runAllTests();
    return 0;
}
