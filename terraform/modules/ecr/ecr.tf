#tfsec:ignore:aws-ecr-enforce-immutable-repository
resource "aws_ecr_repository" "gdx_data_share_poc" {
  name = "gdx-data-share-poc"

  image_scanning_configuration {
    scan_on_push = true
  }
  encryption_configuration {
    encryption_type = "KMS"
    kms_key         = aws_kms_key.ecr_key.arn
  }
}

resource "aws_kms_key" "ecr_key" {
  description         = "Key used to encrypt ECR repository"
  enable_key_rotation = true
}

resource "aws_kms_alias" "ecr_key_alias" {
  name          = "alias/ecr-key"
  target_key_id = aws_kms_key.ecr_key.arn
}

resource "aws_ecr_registry_scanning_configuration" "ecr_scanning_configuration" {
  scan_type = "BASIC"

  rule {
    scan_frequency = "SCAN_ON_PUSH"
    repository_filter {
      filter      = aws_ecr_repository.gdx_data_share_poc.name
      filter_type = "WILDCARD"
    }
  }
}

resource "aws_ecr_pull_through_cache_rule" "quay" {
  ecr_repository_prefix = "quay"
  upstream_registry_url = "quay.io"
}

#these images are hosted and provided externally so we don't enforce scanning or immutable tags
#tfsec:ignore:aws-ecr-enforce-immutable-repository tfsec:ignore:aws-ecr-enable-image-scans
resource "aws_ecr_repository" "lev_api" {
  name = "quay/ukhomeofficedigital/lev-api"

  image_scanning_configuration {
    scan_on_push = false
  }
  encryption_configuration {
    encryption_type = "KMS"
    kms_key         = aws_kms_key.ecr_key.arn
  }
}
