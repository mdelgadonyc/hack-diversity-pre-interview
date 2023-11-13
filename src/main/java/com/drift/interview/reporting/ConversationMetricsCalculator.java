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
