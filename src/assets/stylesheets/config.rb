require 'java'

asset_pipe_path = Java::AssetPipeline::AssetPipelineConfigHolder.getResolvers()[0].getBaseDirectory().getCanonicalPath()
images_path = "#{asset_pipe_path}/images"

relative_assets = false
http_images_path = "./"