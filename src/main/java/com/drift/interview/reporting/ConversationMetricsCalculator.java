package com.drift.interview.reporting;

import com.drift.interview.model.Conversation;
import com.drift.interview.model.ConversationResponseMetric;
import com.drift.interview.model.Message;
import java.util.List;

public class ConversationMetricsCalculator {
  public ConversationMetricsCalculator() {}

  /**
   * Returns a ConversationResponseMetric object which can be used to power data visualizations on the front end.
   */
  ConversationResponseMetric calculateAverageResponseTime(Conversation conversation) {
    List<Message> messages = conversation.getMessages();

    // implement me!

    // [Message{conversationId=2, id=1971156513407663, createdAt=0, authorId=9437, teamMember=false, message=Hello},
    // Message{conversationId=2, id=1971156513410432, createdAt=2000, authorId=9437, teamMember=false, message=I'd like to know more about Thneeds},
    // Message{conversationId=2, id=1971156513410826, createdAt=6000, authorId=5, teamMember=true, message=I'm here to help. What's your question?},
    // Message{conversationId=2, id=1971156513411153, createdAt=6500, authorId=9437, teamMember=false, message=What can it do?},
    // Message{conversationId=2, id=1971156513411825, createdAt=8500, authorId=5, teamMember=true, message=The real question is what can't it do},
    // Message{conversationId=2, id=1971156513411826, createdAt=9500, authorId=5, teamMember=true, message=A Thneed can do anything you need!}]

    long userTime = 0;
    boolean waiting = true;

    List<Long> responseTimes = new java.util.ArrayList<>();

    for (Message message : messages) {
      if (!message.isTeamMember()) {
        if (!waiting) {
          userTime = message.getCreatedAt();
          waiting = true;
        }
      }
      else {
        if (waiting) {
          long responseTime = message.getCreatedAt() - userTime;
          responseTimes.add(responseTime);
          waiting = false;
        }
      }
    }

    long totalTime = responseTimes.stream().mapToLong(Long::longValue).sum();
    double response_num = responseTimes.size();
    double averageTime = totalTime / response_num;

    return ConversationResponseMetric.builder()
        .setConversationId(conversation.getId())
        .setAverageResponseMs(averageTime)
        .build();
  }
}
