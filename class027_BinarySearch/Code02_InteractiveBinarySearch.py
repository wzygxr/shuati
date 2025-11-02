"""
交互式二分查找算法实现 (Python版本)

核心思想：
1. 通过与用户交互来确定目标值的位置
2. 每次询问用户目标值与当前猜测值的关系
3. 根据用户反馈调整搜索范围

应用场景：
1. 猜数字游戏
2. 交互式问题求解
3. 自适应查询系统

工程化考量：
1. 用户输入验证
2. 异常处理
3. 查询次数统计
4. 信息论下界计算
"""

import math

class InteractiveBinarySearch:
    """
    交互式二分查找算法类
    """
    
    @staticmethod
    def interactive_binary_search(n):
        """
        交互式二分查找
        
        Args:
            n: 数组大小（范围为1到n）
            
        Returns:
            目标值
            
        时间复杂度：O(log n)
        空间复杂度：O(1)
        """
        left = 1
        right = n
        query_count = 0
        
        print(f"请想象一个1到{n}之间的数字，我将通过二分查找来猜出它。")
        print("对于我的每次猜测，请输入：")
        print("1 - 如果我猜的数字比你想的数字小")
        print("2 - 如果我猜的数字比你想的数字大")
        print("3 - 如果我猜对了")
        print()
        
        while left <= right:
            mid = left + (right - left) // 2
            query_count += 1
            
            print(f"第{query_count}次猜测：{mid}")
            response = input("请输入你的反馈（1/2/3）：")
            
            try:
                response = int(response)
            except ValueError:
                print("输入无效，请输入1、2或3。")
                query_count -= 1  # 不计入查询次数
                continue
            
            if response == 1:  # 猜的数字比目标小
                left = mid + 1
            elif response == 2:  # 猜的数字比目标大
                right = mid - 1
            elif response == 3:  # 猜对了
                print(f"太好了！我用了{query_count}次猜测找到了答案：{mid}")
                return mid
            else:
                print("输入无效，请输入1、2或3。")
                query_count -= 1  # 不计入查询次数
        
        print("无法找到答案，请检查你的反馈是否正确。")
        return -1
    
    @staticmethod
    def calculate_lower_bound(n):
        """
        计算信息论下界（最小查询次数）
        
        Args:
            n: 搜索范围大小
            
        Returns:
            理论最小查询次数
        """
        # 信息论下界：log2(n) 向上取整
        return math.ceil(math.log2(n))
    
    @staticmethod
    def adaptive_search(n):
        """
        自适应查询优化版本
        根据历史查询结果调整查询策略
        
        Args:
            n: 数组大小（范围为1到n）
            
        Returns:
            目标值
        """
        left = 1
        right = n
        query_count = 0
        
        print(f"请想象一个1到{n}之间的数字，我将通过自适应查询来猜出它。")
        print("对于我的每次猜测，请输入：")
        print("1 - 如果我猜的数字比你想的数字小")
        print("2 - 如果我猜的数字比你想的数字大")
        print("3 - 如果我猜对了")
        print()
        
        # 计算理论下界
        lower_bound = InteractiveBinarySearch.calculate_lower_bound(n)
        print(f"理论最小查询次数：{lower_bound}")
        print()
        
        while left <= right:
            # 自适应选择查询点
            # 简单策略：根据剩余范围的中点选择
            range_size = right - left + 1
            mid = left + range_size // 2
            
            query_count += 1
            
            print(f"第{query_count}次猜测：{mid}")
            response = input("请输入你的反馈（1/2/3）：")
            
            try:
                response = int(response)
            except ValueError:
                print("输入无效，请输入1、2或3。")
                query_count -= 1  # 不计入查询次数
                continue
            
            if response == 1:  # 猜的数字比目标小
                left = mid + 1
            elif response == 2:  # 猜的数字比目标大
                right = mid - 1
            elif response == 3:  # 猜对了
                print(f"太好了！我用了{query_count}次猜测找到了答案：{mid}")
                efficiency = query_count / lower_bound
                print(f"查询效率：{efficiency:.2f}倍理论下界")
                return mid
            else:
                print("输入无效，请输入1、2或3。")
                query_count -= 1  # 不计入查询次数
        
        print("无法找到答案，请检查你的反馈是否正确。")
        return -1


# 主函数
if __name__ == "__main__":
    n = int(input("请输入数字范围的上限（例如100）："))
    
    print("请选择查询策略：")
    print("1 - 标准二分查找")
    print("2 - 自适应查询")
    choice = int(input("请输入选择（1或2）："))
    
    if choice == 1:
        InteractiveBinarySearch.interactive_binary_search(n)
    elif choice == 2:
        InteractiveBinarySearch.adaptive_search(n)
    else:
        print("无效选择，使用标准二分查找。")
        InteractiveBinarySearch.interactive_binary_search(n)