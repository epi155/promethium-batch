MAIN = batch-ug-it
LATEXOPTS = -shell-escape --interaction nonstopmode -halt-on-error -file-line-error

all: ${MAIN}.pdf

${MAIN}.pdf: ${MAIN}.tex 000-pre.tex 001-title.tex 010-intro.tex \
020-job.tex 030-step.tex
	pdflatex ${LATEXOPTS} '$<'
	makeindex '$(basename $<).idx'
	pdflatex ${LATEXOPTS} '$<'
	pdflatex ${LATEXOPTS} '$<'

clean:
	rm -f *.aux *.idx *.ilg *.ind *.lof *.log *.lol *.lot *.out *.toc
	rm -rf _minted-*
