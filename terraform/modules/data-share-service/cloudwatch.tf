resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "${var.environment}-gdx-data-share-poc-ecs-logs"
  retention_in_days = var.cloudwatch_retention_period

  kms_key_id = aws_kms_key.log_key.arn
}

resource "aws_cloudwatch_log_group" "lb_sg_update" {
  name              = "/aws/lambda/${aws_lambda_function.lb_sg_update.function_name}"
  retention_in_days = var.cloudwatch_retention_period

  kms_key_id = aws_kms_key.log_key.arn
}