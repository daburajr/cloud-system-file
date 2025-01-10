provider "aws" {
  region = var.region 
}

resource "aws_key_pair" "default" {
  key_name   = var.key_name 
  public_key = file(var.path_public_key) 
}

resource "aws_security_group" "allow_ports" {
  name_prefix = "allow-ports-"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] 
  }

  ingress {
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "ec2_instance" {
  ami           = var.ec2_instance_ami
  instance_type = var.ec2_instance_type            
  key_name      = aws_key_pair.default.key_name

  security_groups = [
    aws_security_group.allow_ports.name
  ]

  user_data = <<-EOF
            #!/bin/bash
            sudo yum update -y
            sudo yum install -y java-21-amazon-corretto-headless
            EOF

  tags = {
    Name = var.ec2_instance_name
  }
}

output "instance_public_dns" {
  value = aws_instance.ec2_instance.public_dns
}

output "instance_public_ip" {
  value = aws_instance.ec2_instance.public_ip
}
