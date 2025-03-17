import React from "react";
import { CheckCircle, XCircle, Clock } from "lucide-react";

const StatusIcon = ({ status }) => {
  switch (status) {
    case "approved":
      return <CheckCircle className="status-icon approved" />;
    case "rejected":
      return <XCircle className="status-icon rejected" />;
    case "pending":
      return <Clock className="status-icon pending" />;
    default:
      return null;
  }
};

export default StatusIcon;
