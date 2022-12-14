#TODO-https://github.com/alphagov/gdx-data-share-poc/issues/20: When we have our own route53 we can specify certs and not use default
#tfsec:ignore:aws-cloudfront-use-secure-tls-policy
resource "aws_cloudfront_distribution" "gdx_data_share_poc" {
  provider = aws.us-east-1

  origin {
    domain_name = aws_lb.load_balancer.dns_name
    origin_id   = "${var.environment}-gdx-data-share-poc-lb"

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "http-only"
      # Required but irrelevant - we do not use HTTPS to talk to LB
      origin_ssl_protocols = ["TLSv1.2"]
      # AWS enforce a maximum of 60s, but we can request more if desired.
      # See https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/distribution-web-values-specify.html#DownloadDistValuesOriginResponseTimeout
      origin_read_timeout = 60
    }
  }

  logging_config {
    include_cookies = false
    bucket          = "${aws_s3_bucket.cloudfront_logs_bucket.bucket}.s3.amazonaws.com"
    prefix          = var.environment
  }

  enabled         = true
  is_ipv6_enabled = false

  default_cache_behavior {
    allowed_methods  = ["GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = "${var.environment}-gdx-data-share-poc-lb"

    forwarded_values {
      query_string = true
      headers      = ["*"]
      cookies {
        forward = "all"
      }
    }

    # Auto-compress stuff if given Accept-Encoding: gzip header
    compress = true

    viewer_protocol_policy = "redirect-to-https"
    min_ttl                = 0
    default_ttl            = 3600
    max_ttl                = 86400
  }

  # This is the lowest class and only offers nodes in Europe, US and Asia.
  # Given that most of our traffic is from the UK, this should be sufficient.
  price_class = "PriceClass_100"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  web_acl_id = aws_wafv2_web_acl.gdx_data_share_poc.arn
}