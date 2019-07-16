package xin.zero2one.ds.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJD
 * @date 2019/4/22
 */
public class _0002_AddTwoNum {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);
        ListNode listNode = addTwoNumbers(l1, l2);
        traverse(listNode);
    }

    /**
     * 问题描述：
     * You are given two non-empty linked lists representing two non-negative integers.
     * The digits are stored in reverse order and each of their nodes contain a single digit.
     * Add the two numbers and return it as a linked list.
     *
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     *
     * 给出两个 非空 的链表用来表示两个非负的整数。
     * 其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
     * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
     *
     * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     *
     * Example:
     *
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     * Explanation: 342 + 465 = 807.
     *
     * 思路：
     * 1. 同时遍历链表
     * 2. 当其中一个链表遍历结束，另外一个链表未遍历结束时，补 0
     * 3. 值相加，模 10 取余，作为进位
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2){
        ListNode sum = new ListNode(0);
        ListNode head = sum;
        int carry = 0;
        while (null != l1 || null != l2){
            int l1Val;
            if (null == l1){
                l1Val = 0;
            } else {
                l1Val = l1.val;
            }
            int l2Val;
            if (null == l2){
                l2Val = 0;
            } else {
                l2Val = l2.val;
            }
            int sumVal = l1Val + l2Val + carry;
            carry = sumVal % 10;
            int val = sumVal / 10;
            sum.val = val;
            sum = sum.next;
        }
        if (carry > 0){
            while (carry / 10 != 0){
                carry = carry / 10;
                sum.val = carry;
                sum = sum.next;
            }
            sum.val = carry;
        }
        return head;
    }


    private static void traverse(ListNode node){
        while(null != node){
            System.out.println(node.val);
        }
    }



    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

}
